package ink.teamwork.model;

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

}
