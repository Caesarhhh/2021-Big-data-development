package com.bigdatapt.homework.homework1;

import com.bigdatapt.homework.homework1.config.S3Excutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class Homework1Application {

	public static void main(String[] args) {
		//SpringApplication.run(Homework1Application.class, args);
		S3Excutor s3Excutor=new S3Excutor();
	}

}
