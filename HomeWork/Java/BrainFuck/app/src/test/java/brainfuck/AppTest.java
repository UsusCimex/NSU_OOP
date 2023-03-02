/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package brainfuck;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import brainfuck.data.CommandContext;
import brainfuck.logic.OperationFactory;
import brainfuck.operation.Operation;
import brainfuck.data.RegisterTape;
import brainfuck.data.StackWhile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

@TestInstance(Lifecycle.PER_CLASS)
public class AppTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayInputStream inContent = new ByteArrayInputStream("10".getBytes());
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);
    }
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    CommandContext cc;
    @BeforeAll
    public void init() throws IOException{
        try {
            cc = new CommandContext("src/test/resources/forTest.txt");
        }
        catch(FileNotFoundException ex) {
            throw new RuntimeException("File cannot be opened!");
        }
        System.setProperty("log4j.configurationFile", "log4j2.xml");
    }

    @Test 
    public void TestOperationPlus() {
        RegisterTape.GetInstance().resetTape();

        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());
        
        Operation a = OperationFactory.GetInstance().create("+");
        a.run(cc);

        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());

        a.run(cc);
        a.run(cc);

        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(3, RegisterTape.GetInstance().getCellValue());
    }

    @Test 
    public void TestOperationMinus() {
        RegisterTape.GetInstance().resetTape();

        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());

        Operation a = OperationFactory.GetInstance().create("-");
        a.run(cc);

        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(-1, RegisterTape.GetInstance().getCellValue());

        a.run(cc);
        a.run(cc);

        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(-3, RegisterTape.GetInstance().getCellValue());
    }

    @Test 
    public void TestOperationNext() {
        RegisterTape.GetInstance().resetTape();

        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());

        Operation a = OperationFactory.GetInstance().create(">");
        a.run(cc);

        Assertions.assertEquals(RegisterTape.GetInstance().getCellIndex(), 1);
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());

        a.run(cc);
        a.run(cc);

        Assertions.assertEquals(3, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());
    }

    @Test 
    public void TestOperationBack() {
        RegisterTape.GetInstance().resetTape();

        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());

        Operation a = OperationFactory.GetInstance().create(">");
        a.run(cc);
        a.run(cc);
        a.run(cc);

        Assertions.assertEquals(3, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());

        a = OperationFactory.GetInstance().create("<");
        a.run(cc);

        Assertions.assertEquals(2, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());

        a.run(cc);
        a.run(cc);

        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());
    }
    
    @Test 
    public void TestLoop() {
        RegisterTape.GetInstance().resetTape();
        StackWhile.GetInstance().resetStack();

        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());

        Assertions.assertEquals(cc.pointer, 0);
        Operation a = OperationFactory.GetInstance().create("[");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> { a.run(cc); } );

        try {
            cc.ChangeFileForTest("src/test/resources/loopTest.txt");
        }
        catch(FileNotFoundException ex) {
            return;
        }
        
        cc.pointer = 0;
        a.run(cc);
        Assertions.assertEquals(3, cc.pointer);

        cc.pointer = 0;
        RegisterTape.GetInstance().setCellIndex(0);
        RegisterTape.GetInstance().setCellValue(1);
        a.run(cc);
        Assertions.assertEquals(1, cc.pointer);
        Assertions.assertEquals(0, StackWhile.GetInstance().top().from());
        Assertions.assertEquals(0, StackWhile.GetInstance().top().to());

        cc.pointer = 2;
        RegisterTape.GetInstance().setCellIndex(2);
        RegisterTape.GetInstance().setCellValue(0);
        Assertions.assertEquals(2, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());
        Operation b = OperationFactory.GetInstance().create("]");
        b.run(cc);
        Assertions.assertEquals(cc.pointer, 3);

        cc.pointer = 0;
        RegisterTape.GetInstance().setCellIndex(0);
        a.run(cc);
        cc.pointer = 2;
        RegisterTape.GetInstance().setCellIndex(2);
        RegisterTape.GetInstance().setCellValue(1);
        b.run(cc);
        Assertions.assertEquals(0, cc.pointer);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> { b.run(cc); });
    }

    @Test
    public void TestInputAndOutput() {
        setUpStreams();
        RegisterTape.GetInstance().resetTape();

        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellIndex());
        Assertions.assertEquals(0, RegisterTape.GetInstance().getCellValue());

        Operation op1 = OperationFactory.GetInstance().create(",");
        op1.run(cc);

        Assertions.assertEquals(RegisterTape.GetInstance().getCellValue(), 10);

        Operation op2 = OperationFactory.GetInstance().create(".");
        op2.run(cc);
        Assertions.assertEquals(49, outContent.toByteArray()[0]); //49 == '1'
        Assertions.assertEquals(48, outContent.toByteArray()[1]); //49 == '0'
        op2.run(cc);
        Assertions.assertEquals(49, outContent.toByteArray()[3]);
        Assertions.assertEquals(49, outContent.toByteArray()[4]);

        restoreStreams();
    }
}