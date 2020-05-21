package space.netbytes.adshunters.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenExeption extends Exception{

    public ForbiddenExeption(String message){
        super(message);
    }
}
