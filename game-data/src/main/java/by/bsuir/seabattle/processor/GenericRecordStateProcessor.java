package by.bsuir.seabattle.processor;

import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;

@RequiredArgsConstructor
public class GenericRecordStateProcessor implements Processor<String, GenericRecord, Void, Void> {

    private final String storeName;

    private KeyValueStore<String, GenericRecord> stateStore;


    @Override
    public void init(ProcessorContext context) {
        stateStore = (KeyValueStore<String, GenericRecord>) context.getStateStore(storeName);
    }

    @Override
    public void process(Record record) {
        stateStore.put((String) record.key(), (GenericRecord) record.value());
    }

    @Override
    public void close() {
        Processor.super.close();
    }
}
