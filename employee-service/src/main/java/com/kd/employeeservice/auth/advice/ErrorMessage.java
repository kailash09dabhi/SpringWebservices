package com.kd.employeeservice.auth.advice;

import java.util.Date;

public record ErrorMessage(int statusCode, Date timestamp, String message, String description) {
}