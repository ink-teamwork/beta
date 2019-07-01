package ink.teamwork.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import ink.teamwork.entity.Image;
import ink.teamwork.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;

@Controller
@RequestMapping("/image")
public class ImageController extends BaseController {

    @Autowired
    private ImageRepository repository;

    @ResponseBody
    @PostMapping("/upload")
    public JSONObject upload() throws IOException, ServletException {
        Part part = (Part) request.getParts().toArray()[0];
        Image image = new Image();
        repository.save(image);
        FileUtil.writeFromStream(part.getInputStream(), "C:\\Users\\zhaokai\\Projects\\beta\\src\\main\\resources\\public\\img\\" + image.getId() + ".png");
        System.out.println(request);
        JSONObject result = new JSONObject();
        result.put("url", "http://192.168.3.124:8081/api/img/" + image.getId() + ".png");
        return result;
    }

    @GetMapping("/{id}")
    public void view(@PathVariable String id) throws IOException {
        byte[] b = FileUtil.readBytes("C:\\Users\\zhaokai\\Desktop\\images\\" + id);
        response.getOutputStream().write(b);
    }


}
