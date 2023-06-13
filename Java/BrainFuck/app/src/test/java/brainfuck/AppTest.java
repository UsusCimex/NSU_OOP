/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package brainfuck;

import org.junit.jupiter.api.*;

import brainfuck.data.CommandContext;
import brainfuck.logic.OperationFactory;
import brainfuck.operation.Operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;

public class AppTest {
    @Test 
    public void TestOperationPlus() throws FileNotFoundException {
        CommandContext cc = new CommandContext("src/test/resources/forTest.txt");

        assertEquals(0, cc.registerTape.getCellIndex());
        assertEquals(0, cc.registerTape.getCellValue());
        
        Operation a = OperationFactory.GetInstance().getOperation("+");
        a.run(cc);

        assertEquals(0, cc.registerTape.getCellIndex());
        assertEquals(1, cc.registerTape.getCellValue());

        a.run(cc);
        a.run(cc);

        assertEquals(0, cc.registerTape.getCellIndex());
        assertEquals(3, cc.registerTape.getCellValue());
    }

    @Test 
    public void TestOperationMinus() throws FileNotFoundException {
        CommandContext cc = new CommandContext("src/test/resources/forTest.txt");

        assertEquals(0, cc.registerTape.getCellIndex());
        assertEquals(0, cc.registerTape.getCellValue());

        Operation a = OperationFactory.GetInstance().getOperation("-");
        a.run(cc);

        assertEquals(0, cc.registerTape.getCellIndex());
        assertEquals(-1, cc.registerTape.getCellValue());

        a.run(cc);
        a.run(cc);

        assertEquals(0, cc.registerTape.getCellIndex());
        assertEquals(-3, cc.registerTape.getCellValue());
    }

    @Test 
    public void TestOperationNext() throws FileNotFoundException {
        CommandContext cc = new CommandContext("src/test/resources/forTest.txt");

        assertEquals(0, cc.registerTape.getCellIndex());
        assertEquals(0, cc.registerTape.getCellValue());

        Operation a = OperationFactory.GetInstance().getOperation(">");
        a.run(cc);

        assertEquals(cc.registerTape.getCellIndex(), 1);
        assertEquals(0, cc.registerTape.getCellValue());

        a.run(cc);
        a.run(cc);

        assertEquals(3, cc.registerTape.getCellIndex());
        assertEquals(0, cc.registerTape.getCellValue());
    }

    @Test 
    public void TestOperationBack() throws FileNotFoundException {
        CommandContext cc = new CommandContext("src/test/resources/forTest.txt");

        assertEquals(0, cc.registerTape.getCellIndex());
        assertEquals(0, cc.registerTape.getCellValue());

        Operation a = OperationFactory.GetInstance().getOperation(">");
        a.run(cc);
        a.run(cc);
        a.run(cc);

        assertEquals(3, cc.registerTape.getCellIndex());
        assertEquals(0, cc.registerTape.getCellValue());

        a = OperationFactory.GetInstance().getOperation("<");
        a.run(cc);

        assertEquals(2, cc.registerTape.getCellIndex());
        assertEquals(0, cc.registerTape.getCellValue());

        a.run(cc);
        a.run(cc);

        assertEquals(0, cc.registerTape.getCellIndex());
        assertEquals(0, cc.registerTape.getCellValue());
    }
    
    @Test 
    public void TestLoop() throws FileNotFoundException {
        CommandContext cc = new CommandContext("src/test/resources/forTest.txt");

        assertEquals(0, cc.registerTape.getCellIndex());
        assertEquals(0, cc.registerTape.getCellValue());

        assertEquals(cc.pointer, 0);
        Operation a = OperationFactory.GetInstance().getOperation("[");
        assertThrows(IndexOutOfBoundsException.class, () -> { a.run(cc); } );

        cc.ChangeFileForTest("src/test/resources/loopTest.txt");
        
        cc.pointer = 0;
        a.run(cc);
        assertEquals(3, cc.pointer);

        cc.pointer = 0;
        cc.registerTape.setCellIndex(0);
        cc.registerTape.setCellValue(1);
        a.run(cc);
        assertEquals(1, cc.pointer);
        assertEquals(0, cc.stackWhile.top().from());
        assertEquals(2, cc.stackWhile.top().to());

        cc.pointer = 2;
        cc.registerTape.setCellIndex(2);
        cc.registerTape.setCellValue(0);
        assertEquals(2, cc.registerTape.getCellIndex());
        assertEquals(0, cc.registerTape.getCellValue());
        Operation b = OperationFactory.GetInstance().getOperation("]");
        b.run(cc);
        assertEquals(cc.pointer, 3);

        cc.pointer = 0;
        cc.registerTape.setCellIndex(0);
        a.run(cc);
        cc.pointer = 2;
        cc.registerTape.setCellIndex(2);
        cc.registerTape.setCellValue(1);
        b.run(cc);
        assertEquals(0, cc.pointer);
        assertThrows(IndexOutOfBoundsException.class, () -> { b.run(cc); });
    }

    @Test
    public void TestInput() throws FileNotFoundException {
        CommandContext cc = new CommandContext("src/test/resources/loopTest.txt");

        assertEquals(0, cc.registerTape.getCellIndex());
        assertEquals(0, cc.registerTape.getCellValue());

        cc.ChangeInputStreamFileForTest("src/test/resources/forTest.txt");

        Operation op1 = OperationFactory.GetInstance().getOperation(",");
        op1.run(cc);

        assertEquals(10, cc.registerTape.getCellValue());

        op1.run(cc);
        assertEquals(11, cc.registerTape.getCellValue());
    }
}