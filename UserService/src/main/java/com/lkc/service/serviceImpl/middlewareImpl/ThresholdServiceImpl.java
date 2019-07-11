package com.lkc.service.serviceImpl.middlewareImpl;

import com.lkc.model.industry.sensorInfo.SensorInfo;
import com.lkc.model.userInfo.UserContact;
import com.lkc.repository.IndustryRepository;
import com.lkc.service.serviceInterface.User.UserContactService;
import com.lkc.tool.MyThreadPoolExecutor;
import com.lkc.tool.TimeChange;
import com.lkc.FeignClient.mongoservice.SensorDao;
import com.lkc.service.serviceInterface.MqttService.ThresholdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ThresholdServiceImpl implements ThresholdService {
    @Resource
    private IndustryRepository industryRepository;
    @Resource
    private SensorDao sensorDao;
    @Resource
    private UserContactService userContactService;

    @Override
    public void thresholdHandler(String deviceId, Map<String, String> map, String time) {

        MyThreadPoolExecutor.getInstance().getMyThreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String title = "Spider IoT平台预警邮件";
                StringBuilder content = new StringBuilder();

                //查询 device 下的所有 sensor
                List<SensorInfo> sensorInfos = sensorDao.findAll(deviceId);
                //是否发送 warn email 的标志
                boolean send_flg = false;
                String industryId = null;

                for (SensorInfo sensorInfo : sensorInfos) {
                    if (null == industryId) {
                        industryId = sensorInfo.getIndustryId();
                    }

                    String type = sensorInfo.getType();
                    if (map.get(type) == null || sensorInfo.getMax().isEmpty() || sensorInfo.getMin().isEmpty())
                        continue;
                    int int_max = Integer.valueOf(sensorInfo.getMax());
                    int int_min = Integer.valueOf(sensorInfo.getMin());
                    Float max = Float.valueOf(sensorInfo.getMax());
                    Float min = Float.valueOf(sensorInfo.getMin());
                    Float value = Float.valueOf(map.get(type));
                    if (value > max && int_max != 0) {
                        send_flg = true;
                        content.append("  ").append(type).append(" 值为：").append(value).append(" 超过上限！\n");
                    } else if (value < min && int_min != 0) {
                        send_flg = true;
                        content.append("  ").append(type).append(" 值为：").append(value).append(" 超过下限！\n");
                    }
                }

                if (send_flg) {
                    //查找产业的拥有者
                    List<String> users = industryRepository.findByIndustryId(industryId);

                    for (String name : users) {
                        //查找拥有者的个人信息
                        UserContact user = userContactService.findByName(name);

                        if ("on".equals(user.getEmailctl())) {
                            StringBuilder sendData = new StringBuilder();
                            sendData.append("您好，").append(name + "\n")
                                    .append("您的项目（").append(industryId).append("）拥有的设备：").append(deviceId).append(" 于 ").append(TimeChange.secondToTime(time)).append(" 发出报警：\n")
                                    .append(content);
                            userContactService.sendMail(title, sendData.toString(), user.getEmail());
                        }
                    }
                }
            }
        });
    }
}
