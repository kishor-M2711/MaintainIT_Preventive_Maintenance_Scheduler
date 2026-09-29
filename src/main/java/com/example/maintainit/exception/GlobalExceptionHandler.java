package com.example.maintainit.exception;
import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.*;
@RestControllerAdvice public class GlobalExceptionHandler  {
    @ExceptionHandler(ResourceNotFoundException.class) ResponseEntity<?> nf(ResourceNotFoundException e) {
        return body(404,e.getMessage());
    }
    @ExceptionHandler(BusinessRuleException.class) ResponseEntity<?> br(BusinessRuleException e) {
        return body(400,e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> val(MethodArgumentNotValidException e) {
        Map<String,Object> b=new LinkedHashMap<>();
        b.put("timestamp",LocalDateTime.now());
        b.put("status",400);
        b.put("error","Validation failed");
        Map<String,String> f=new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x->f.put(x.getField(),x.getDefaultMessage()));
        b.put("fields",f);
        return ResponseEntity.badRequest().body(b);
    }
    private ResponseEntity<?> body(int s,String m) {
        Map<String,Object>b=new LinkedHashMap<>();
        b.put("timestamp",LocalDateTime.now());
        b.put("status",s);
        b.put("error",m);
        return ResponseEntity.status(s).body(b);
    }
}
