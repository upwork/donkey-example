package com.example.zoo.messageconverter;

import org.apache.commons.io.IOUtils;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.transport.TTransportException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

public class ThriftJsonMessageConverter extends AbstractHttpMessageConverter<TBase> {
    private static final int INITIAL_MEMORY_BUFFER_SIZE = 1;

    public ThriftJsonMessageConverter() {
        super(new MediaType("application", "vnd.apache.thrift.json"));
    }

    @Override
    protected boolean supports(Class<?> type) {
        return TBase.class.isAssignableFrom(type);
    }

    @Override
    protected TBase readInternal(Class<? extends TBase> type, HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException {
        byte[] entityBytes = IOUtils.toByteArray(inputMessage.getBody());
        try (TMemoryBuffer memoryBuffer = new TMemoryBuffer(entityBytes.length)) {
            TBase entity = null;
            try {
                memoryBuffer.write(entityBytes);
                entity = type.getConstructor().newInstance();
            } catch (InstantiationException | InvocationTargetException | TTransportException |
                    NoSuchMethodException | IllegalAccessException e) {
                throw new IOException(e);
            }

            try {
                entity.read(new TJSONProtocol(memoryBuffer));
            } catch (TException e) {
                throw new HttpMessageNotReadableException("Failed to deserialize Thrift", e);
            }
            return entity;
        }
    }

    @Override
    protected void writeInternal(TBase entity, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        try (TMemoryBuffer memoryBuffer = new TMemoryBuffer(INITIAL_MEMORY_BUFFER_SIZE)) {
            entity.write(new TJSONProtocol(memoryBuffer));
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
