package com.axana.nifi.processors;


import java.util.Collections;

import org.apache.nifi.processor.Processor;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Before;
import org.junit.Test;

public class AxanaMyHL7ProcessorTest {
    
    private TestRunner testRunner;

    @Before
    public void setUp() {
        Processor processor = new AxanaHL7ToJsonExtracter_1_0_9(); // Replace with your processor
        testRunner = TestRunners.newTestRunner(processor);
    }

    @Test
    public void testProcessorWithHL7Message() {
        String hl7Message = "MSH|^~\\&|SendingApp|SendingFac|ReceivingApp|ReceivingFac|20231022000000||ORM^O01|123456|P|2.3";
/* 
        String hl7Message = "MSH|^~\\&|MESA_OP|XYZ_HOSPITAL|iFW|ABC_HOSPITAL|20110613061611||SIU^S12|24916560|P|2.3||||||\r"
        + //
        "SCH|10345^10345|2196178^2196178|||10345|OFFICE^Office visit|^reason for the appointment|OFFICE|60|m|^^60^20110617084500^20110617093000|||||9^DENT^ARTHUR^||||9^DENT^COREY^|||||Scheduled\r"
        + //
        "PID|1||42||SMITH^PAUL||19781012|M|||1 Broadway Ave^^Fort Wayne^IN^46804||(260)555-1234|||S||999999999|||||||||||||||||||||\r"
        + //
        "PV1|1|O|||||1^Smith^Miranda^A^MD^^^^|2^Withers^Peter^D^MD^^^^||||||||||||||||||||||||||||||||||||||||||99158||\r"
        + //
        "RGS|1|A\r" + //
        "AIG|1|A|1^White, Charles|D^^\r" + //
        "AIL|1|A|OFFICE^^^OFFICE|^Main Office||20110614084500|||45|m^Minutes||Scheduled\r" + //
        "AIP|1|A|1^White^Charles^A^MD^^^^|D^White, Douglas||20110614084500|||45|m^Minutes||Scheduled";
*/
        testRunner.enqueue(hl7Message.getBytes(), Collections.singletonMap("mime.type", "application/hl7"));

        testRunner.run();

        // Validate the output
        testRunner.assertAllFlowFilesTransferred(AxanaHL7ToJsonExtracter_1_0_9.SUCCESS);
        // Add assertions based on what your processor should do
           // assertEquals(expected, actual);
    }
}
