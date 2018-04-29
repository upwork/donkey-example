package com.example.zoo.messageconverter;

import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.apache.thrift.transport.TMemoryBuffer;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;

public class JsonMessageConverter extends AbstractHttpMessageConverter<TBase> {
    private static final int INITIAL_MEMORY_BUFFER_SIZE = 1;

    public JsonMessageConverter() {
        super(new MediaType("application", "json"));
    }

    @Override
    protected boolean supports(Class<?> type) {
        return TBase.class.isAssignableFrom(type);
    }

    @Override
    protected TBase readInternal(Class<? extends TBase> type, HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException {
        throw new HttpMessageNotReadableException("TSimpleJSONProtocol is write-only");
    }

    @Override
    protected void writeInternal(TBase entity, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        try (TMemoryBuffer memoryBuffer = new TMemoryBuffer(INITIAL_MEMORY_BUFFER_SIZE)) {
            entity.write(new TSimpleJSONProtocol(memoryBuffer));
            memoryBuffer.flush();
            OutputStream outputStream = outputMessage.getBody();
            outputStream.write(
                    memoryBuffer.toString("UTF-8").getBytes()
            );
            //outputStream flushing is done by the parent: https://url.upwork.com/_017N1wszJCqiIUTmihutOKvvlrsJwoUmzq
        } catch (TException e) {
            throw new IOException(e);
        }
    }
}
