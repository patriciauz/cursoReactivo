package co.edu.sophos.actividad1.universidad.service;

import co.edu.sophos.actividad1.universidad.config.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CursoKafkaConsumerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CursoKafkaConsumerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String obtenerUltimoCurso(String topico){
        ConsumerRecord<String, String> ultimoCurso;
        KafkaConfig kafkaConfig = new KafkaConfig();
        kafkaTemplate.setConsumerFactory(kafkaConfig.consumerFactory());
        ultimoCurso = kafkaTemplate.receive(topico, 0, 0);
        String cursoRegistrado = Objects.requireNonNull(ultimoCurso.value());
        return cursoRegistrado;
    }
}
