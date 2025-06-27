package com.billy.prueba_tecnica;

import com.billy.prueba_tecnica.service.ReportHtmlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class ProcesadorApplication implements CommandLineRunner {



	public static void main(String[] args) {
		SpringApplication.run(ProcesadorApplication.class, args);
	}


	private final Job job;
	private final JobLauncher jobLauncher;
	private final ReportHtmlGenerator reportHtmlGenerator;
	@Override
	public void run(String... args)  {

		JobParameters parameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(job,parameters);
			reportHtmlGenerator.generateReport();

			//System.exit(0);
		} catch (Exception e) {
			log.error("Error al Ejecutar el proceso {}",e.getMessage(), e);
		}

    }
}