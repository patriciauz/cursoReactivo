package co.edu.sophos.actividad1.universidad.service;

import co.edu.sophos.actividad1.universidad.model.Curso;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CursoSQSService {
    private final AmazonSQS clienteSQS;

    public CursoSQSService(AmazonSQS clienteSQS) {
        this.clienteSQS = clienteSQS;
    }

    public String createStandardQueue(String queueName){

        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        return clienteSQS.createQueue(createQueueRequest).getQueueUrl();
    }

    private String getQueueUrl(String queueName){
        return clienteSQS.getQueueUrl(queueName).getQueueUrl();
    }

    public String publishStandardQueueMessage(String queueName, Integer delaySeconds, Curso curso){
        Map<String, MessageAttributeValue> atributosMensaje = new HashMap<>();

        atributosMensaje.put("id",
                new MessageAttributeValue()
                        .withStringValue(Optional.ofNullable(curso.getId()).orElse(-301).toString())
                        .withDataType("Number"));
        atributosMensaje.put("nombre",
                new MessageAttributeValue()
                        .withStringValue(curso.getNombre())
                        .withDataType("String"));


        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(this.getQueueUrl(queueName))
                .withMessageBody(curso.getNombre())
                .withDelaySeconds(delaySeconds)
                .withMessageAttributes(atributosMensaje);

        return clienteSQS.sendMessage(sendMessageRequest).getMessageId();
    }

    public void publishStandardQueueMessage(String queueName, Integer delaySeconds, List<Curso> cursoList){
        for (Curso curso : cursoList){
            publishStandardQueueMessage(queueName, delaySeconds, curso);
        }
    }

    private List<Message> receiveMessagesFromQueue(String queueName, Integer maxNumberMessages, Integer waitTimeSeconds){
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(this.getQueueUrl(queueName))
                .withMaxNumberOfMessages(maxNumberMessages)
                .withMessageAttributeNames(List.of("All"))
                .withWaitTimeSeconds(waitTimeSeconds);
        return clienteSQS.receiveMessage(receiveMessageRequest).getMessages();
    }

    public Mono<Curso> deleteCursoMessageInQueue(String queueName, Integer maxNumberMessages,
                                                       Integer waitTimeSeconds, String nombreCurso){
        List<Message> productoMessages = receiveMessagesFromQueue(queueName, maxNumberMessages, waitTimeSeconds);
        for(Message message : productoMessages){
            if(!message.getMessageAttributes().isEmpty()) {
                if (message.getMessageAttributes().get("nombre").getStringValue().equals(nombreCurso)) {
                    Curso curso = new Curso(Integer.valueOf(message.getMessageAttributes().get("id").getStringValue()),
                            message.getMessageAttributes().get("nombre").getStringValue());
                    clienteSQS.deleteMessage(this.getQueueUrl(queueName), message.getReceiptHandle());
                    return Mono.just(curso);
                }
            }
        }
        return Mono.empty();
    }
}
