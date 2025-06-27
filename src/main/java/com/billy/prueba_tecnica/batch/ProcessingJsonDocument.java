package com.billy.prueba_tecnica.batch;

import com.billy.prueba_tecnica.batch.listener.ItemProcessorListener;
import com.billy.prueba_tecnica.batch.listener.JobListener;
import com.billy.prueba_tecnica.batch.listener.StepListener;
import com.billy.prueba_tecnica.batch.processor.JsonDocumentProcessor;
import com.billy.prueba_tecnica.batch.reader.DocumentContainerJsonReader;
import com.billy.prueba_tecnica.batch.writer.XmlDocumentExportWrite;
import com.billy.prueba_tecnica.configuration.FileProcessingProperty;
import com.billy.prueba_tecnica.domain.Document;
import com.billy.prueba_tecnica.dto.input.json.JsonDocumentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProcessingJsonDocument {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final FileProcessingProperty fileProcessingProperty;
    private final XmlDocumentExportWrite xmlDocumentExportWrite;
    private final JsonDocumentProcessor jsonDocumentProcessor;
    private final JobListener jobListener;
    private final StepListener stepListener;
    private final ItemProcessorListener itemProcessorListener;

    @Bean
    public Job importDocumentJob(Step importDocumentStep) {
        return new JobBuilder("ProcessingJsonDocument", jobRepository)
                .listener(jobListener)
                .start(importDocumentStep)
                .build();
    }

    @Bean
    public Step importDocumentStep() {
        return new StepBuilder("importDocumentStep",jobRepository)
                .<JsonDocumentDto, Document>chunk(fileProcessingProperty.getChunkSize(), platformTransactionManager)
                .reader(jsonItemReader())
                .processor(jsonDocumentProcessor )
                .listener(itemProcessorListener)
                .writer(xmlDocumentExportWrite::write)
                .listener(stepListener)
                .build();
    }
    public JsonItemReader<JsonDocumentDto> jsonItemReader() {
        return new JsonItemReaderBuilder<JsonDocumentDto>()
                .jsonObjectReader(new DocumentContainerJsonReader())
                .resource(new FileSystemResource(fileProcessingProperty.getFileInProcessingPath()))
                .name("jsonItemReader")
                .saveState(false)
                .build();
    }

}
