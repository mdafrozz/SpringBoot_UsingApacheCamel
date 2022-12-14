package com.example.demo;

import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringCamelApplication extends RouteBuilder {

	public static void main(String[] args) {
		SpringApplication.run(SpringCamelApplication.class, args);
	}

	@Override
	public void configure() throws Exception {
		//Move data from one file to another
				System.out.println("Started..");
				//moveAllFile();
				//moveSpecificFile("myfile");
				//moveSpecificFileWithBody("Java");
				//fileProcess();
				multiFileProcess();
				System.out.println("End..");
			}
			
			public void moveAllFile() {
				from("file:C:/Users/MD_AFROZ/Desktop/a?noop=true").to("file:C:/Users/MD_AFROZ/Desktop/b");
			}
			
			public void moveSpecificFile(String type) {
				from("file:C:/Users/MD_AFROZ/Desktop/a?noop=true").filter(header(Exchange.FILE_NAME).startsWith(type)).to("file:C:/Users/MD_AFROZ/Desktop/b");
			}
			
			public void moveSpecificFileWithBody(String content) {
				from("file:C:/Users/MD_AFROZ/Desktop/a?noop=true").filter(body().startsWith(content)).to("file:C:/Users/MD_AFROZ/Desktop/b");
			}
			
			public void fileProcess() {
				from("file:C:/Users/MD_AFROZ/Desktop/a?noop=true").process(p->{
					String body = p.getIn().getBody(String.class);
					StringBuilder sb = new StringBuilder();
					Arrays.stream(body.split(" ")).forEach(s ->{
						sb.append(s+",");
						});
					p.getIn().setBody(sb);
				})
				.to("file:C:/Users/MD_AFROZ/Desktop/b?fileName=demo.csv");
			}
			
			public void multiFileProcess() {
				from("file:C:/Users/MD_AFROZ/Desktop/a?noop=true").unmarshal().csv().split(body().tokenize(",")).choice()
				.when(body().contains("Closed")).to("file:C:/Users/MD_AFROZ/Desktop/b?fileName=closed.csv")
				.when(body().contains("Pending")).to("file:C:/Users/MD_AFROZ/Desktop/b?fileName=pending.csv")
				.when(body().contains("Interest")).to("file:C:/Users/MD_AFROZ/Desktop/b?fileName=Interest.csv");

			}
		}
