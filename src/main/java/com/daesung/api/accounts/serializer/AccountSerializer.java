package com.daesung.api.accounts.serializer;

import com.daesung.api.accounts.domain.Account;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;


public class AccountSerializer extends JsonSerializer<Account> {

    @Override
    public void serialize(Account account, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
                gen.writeStartObject();
                gen.writeNumberField("id",account.getId());
                gen.writeStringField("loginId",account.getLoginId());
                gen.writeStringField("acName",account.getAcName());
                gen.writeObjectField("roles",account.getRoles());
                gen.writeEndObject();
    }
}
