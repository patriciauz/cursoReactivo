package co.edu.sophos.actividad1.universidad.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CursoKafkaConsumerService {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductoKafkaConsumerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CursoKafkaConsumerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String obtenerUltimoCurso(String topico){
        ConsumerRecord<String, String> ultimoCurso;
        KafkaConfig kafkaConfig = new KafkaConfig();
        kafkaTemplate.setConsumerFactory(kafkaConfig.consumerFactory());
        ultimoProducto = kafkaTemplate.receive(topico, 0, 0);
        String cursoRegistrado = Objects.requireNonNull(ultimoCurso.value());
        return productoRecibido;
    }
}
