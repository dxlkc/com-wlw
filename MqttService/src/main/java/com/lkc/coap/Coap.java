package com.lkc.coap;

import com.lkc.config.CoapConfig;
import net.sf.json.JSONObject;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

public class Coap {
    //日志记录器
    private Logger logger = LoggerFactory.getLogger(getClass());

    public <T> String send(String URI, T object) {
        CoapResponse response = null;

        if (null == URI || "".equals(URI)) {
            URI = Config.getServerIp() + "/shell";
        }

        try {

            URI uri = new URI(URI);
            CoapClient coapClient = new CoapClient(uri);

            JSONObject jsonObject = JSONObject.fromObject(object);
            String request = jsonObject.toString();
            //Base64加密
            Base64.Encoder encoder = Base64.getEncoder();
            request = encoder.encodeToString(request.getBytes());
            response = coapClient.post(request, -1);

        } catch (URISyntaxException e) {
            logger.warn("Coap : URI 创建失败...");
            return "";
        }

        if (null == response) {
            return "";
        }

        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(response.getResponseText()));
    }

    @Component
    private static class Config {
        @Autowired
        private CoapConfig coapConfig;

        private static Config config;

        @PostConstruct
        public void init() {
            config = this;
            config.coapConfig = this.coapConfig;
        }

        static String getServerIp() {
            return config.coapConfig.getServerIp();
        }
    }
}
