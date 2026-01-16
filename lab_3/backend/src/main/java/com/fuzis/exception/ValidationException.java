package com.fuzis.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {
   public ValidationException(String message){
       super(message);
   }
}