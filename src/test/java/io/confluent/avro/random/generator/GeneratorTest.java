package io.confluent.avro.random.generator;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.junit.jupiter.api.Test;
import org.springframework.format.datetime.DateFormatter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorTest {

    @Test
    void testGenerator() throws IOException, ParseException {
        var exampleSchema = getClass().getResource("/example.avsc");
        var schema = new Schema.Parser().parse(new File(exampleSchema.getFile()));
        var target = new Generator(schema, new Random());

        GenericData.Record result = (GenericData.Record) target.generate();
        System.out.println(result);

        assertTrue(result.get("accountClosureDate") == null || isValidDate("dd/MM/yyyy", (String)result.get("accountClosureDate")), "accountClosureDate is not either null or a valid date");
        assertTrue(isValidDate("yyyy-MM-dd'T'HH:mm:ss.SSSZ", (String)result.get("lastLoginTime")), "lastLoginTime is not a valid date");
        assertTrue(isValidDate("yyyy-MM-dd", (String)result.get("signupDate"))
                || isValidDate("dd/MM/yyyy", (String)result.get("signupDate")), "signupDate is not a valid date");

    }

    private boolean isValidDate(String format, String date) {
        try {
            var formatter = new DateFormatter(format);
            formatter.parse(date, null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}