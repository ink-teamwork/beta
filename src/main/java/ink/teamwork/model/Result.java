package ink.teamwork.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Result implements Serializable {

    private Boolean success;

    private String msg;

    private Object data;

    public static Result of(){
        return Result.of(true);
    }

    public static Result of(Boolean bool){
        return Result.builder().success(bool).build();
    }

    public static Result of(Boolean bool, String msg){
        return Result.builder().success(bool).msg(msg).build();
    }

    public static Result of(Boolean bool, String msg, Object data){
        return Result.builder().success(bool).msg(msg).data(data).build();
    }

    private int draw;

    private long recordsTotal;

    private long recordsFiltered;

    public static Result page(int draw, long recordsTotal, long recordsFiltered, Object data){
        return Result.builder()
                .draw(draw)
                .recordsTotal(recordsTotal)
                .recordsFiltered(recordsFiltered)
                .data(data)
                .build();
    }

    public static JSONObject page(int currentPage, int pageSize, long total, Object list){
        JSONObject result = new JSONObject();
        JSONObject pagination = new JSONObject();
        result.put("list", list);
        pagination.put("current", currentPage);
        pagination.put("pageSize", pageSize);
        pagination.put("total", total);
        result.put("pagination", pagination);
        return result;
    }


}
