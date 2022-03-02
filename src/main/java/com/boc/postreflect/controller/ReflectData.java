package com.boc.postreflect.controller;


import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/postreflect", method = {RequestMethod.GET, RequestMethod.POST})
public class ReflectData {

    private String lastData = null;
    private Date lastTime = new Date();
    private final String autoRefresh = "<script type='text/javascript'>function StatusRefresh(){window.location.reload();}setTimeout('StatusRefresh()',2000);</script>";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @RequestMapping("/reflect")
    public String reflect(){
        String dateTime = dateFormat.format(lastTime);

        return "Last received time: " + dateTime + "</br>" + "Post Data is:</br> " + lastData + autoRefresh;
    }

    @PostMapping("/post")
    public String post(HttpServletRequest request){
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        String line = null;
        try {
            request.setCharacterEncoding("UTF-8");
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("</br>");
                }
            } else {
                stringBuilder.append("</br>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        lastData = String.valueOf(stringBuilder);
        lastTime = new Date();

        return "Data has been received";
    }

    @PostMapping("/postjson")
    public String post(@RequestBody(required = false) Object object){
        lastData = object.toString();
        return "Data has been received";
    }

}
