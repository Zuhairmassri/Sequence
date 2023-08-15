package proj2;

import org.junit.*;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class SequenceTest {
    @Rule // a test will fail if it takes longer than 1/10 of a second to run
    public Timeout timeout = Timeout.millis(100);

    @Test//Test toString method, empty sequence, after adding before/after, capacity changes
    public void testToString(){
        Sequence mySeq1 = new Sequence();
        Sequence mySeq2 = new Sequence(2);
        assertEquals("{} (capacity = 10)", mySeq1.toString());
        assertEquals("{} (capacity = 2)", mySeq2.toString());
        mySeq2.addAfter("hi");
        assertEquals("{>hi} (capacity = 2)", mySeq2.toString());
        mySeq2.addAfter("hefe");
        assertEquals("{hi, >hefe} (capacity = 2)", mySeq2.toString());
        mySeq2.addBefore("heyyyyy");
        assertEquals("{hi, >heyyyyy, hefe} (capacity = 5)", mySeq2.toString());
    }

    @Test//Test addBefore method, add to empty sequence, after advancing within/beyond elements
    public void testAddBefore(){
        Sequence mySeq1 = new Sequence(3);
        mySeq1.advance();
        mySeq1.advance();
        mySeq1.addBefore("Zuhair");
        mySeq1.addBefore("MyName");
        mySeq1.addBefore("Hi");
        assertEquals("{>Hi, MyName, Zuhair} (capacity = 3)", mySeq1.toString());
        mySeq1.addBefore("Haha");
        assertEquals("{>Haha, Hi, MyName, Zuhair} (capacity = 7)", mySeq1.toString());
        mySeq1.addBefore("Whatt");
        assertEquals("{>Whatt, Haha, Hi, MyName, Zuhair} (capacity = 7)", mySeq1.toString());
        mySeq1.advance();
        assertEquals("{Whatt, >Haha, Hi, MyName, Zuhair} (capacity = 7)", mySeq1.toString());
        mySeq1.advance();
        mySeq1.advance();
        mySeq1.advance();
        mySeq1.advance();
        mySeq1.advance();
        assertEquals("{Whatt, Haha, Hi, MyName, Zuhair} (capacity = 7)", mySeq1.toString());
        mySeq1.addBefore("Zuhair2");
        assertEquals("{>Zuhair2, Whatt, Haha, Hi, MyName, Zuhair} (capacity = 7)", mySeq1.toString());
        mySeq1.advance();
        mySeq1.advance();
        mySeq1.advance();
        mySeq1.advance();
        mySeq1.advance();
        mySeq1.advance();
        assertEquals("{Zuhair2, Whatt, Haha, Hi, MyName, Zuhair} (capacity = 7)", mySeq1.toString());
        mySeq1.addBefore("LMAO");
        assertEquals("{>LMAO, Zuhair2, Whatt, Haha, Hi, MyName, Zuhair} (capacity = 7)", mySeq1.toString());
        mySeq1.advance();
        mySeq1.advance();
        assertEquals("{LMAO, Zuhair2, >Whatt, Haha, Hi, MyName, Zuhair} (capacity = 7)", mySeq1.toString());
        mySeq1.addBefore("THIS");
        assertEquals("{LMAO, Zuhair2, >THIS, Whatt, Haha, Hi, MyName, Zuhair} (capacity = 15)", mySeq1.toString());
        mySeq1.advance();
        mySeq1.advance();
        assertEquals("{LMAO, Zuhair2, THIS, Whatt, >Haha, Hi, MyName, Zuhair} (capacity = 15)", mySeq1.toString());
    }

    @Test//Test addAfter method, add to empty sequence, after advancing within/beyond elements
    public void testAddAfter(){
        Sequence mySeq = new Sequence(5);
        mySeq.addAfter("Hi");
        assertEquals("Hi", mySeq.getCurrent());
        mySeq.addAfter("myName");
        assertEquals("myName", mySeq.getCurrent());
        mySeq.advance();
        assertEquals(null, mySeq.getCurrent());
        mySeq.addAfter("HEH");
        assertEquals("HEH", mySeq.getCurrent());
        mySeq.advance();
        assertEquals(null, mySeq.getCurrent());

    }

    @Test //Testing advance() empty sequence, after adding elements, advancing beyond elements
    public void testAdvance(){
        Sequence mySeq1 = new Sequence();
        mySeq1.advance();
        assertEquals(null, mySeq1.getCurrent());
        mySeq1.addBefore("Zuhair");
        mySeq1.addBefore("MyName");
        mySeq1.addBefore("Hi");
        mySeq1.addBefore("Whatt");
        mySeq1.addBefore("Hehe");
        assertEquals("Hehe", mySeq1.getCurrent());
        mySeq1.advance();
        assertEquals("Whatt", mySeq1.getCurrent());
        mySeq1.advance();
        assertEquals("Hi", mySeq1.getCurrent());
        mySeq1.advance();
        assertEquals("MyName", mySeq1.getCurrent());
        mySeq1.advance();
        assertEquals("Zuhair", mySeq1.getCurrent());
        mySeq1.advance();
        assertEquals(null, mySeq1.getCurrent());
    }

    @Test //Tests clone() method, test elements stored in each sequence, the currentIndex, and the size
    public void testClone(){
        Sequence mySeq = new Sequence();
        mySeq.addAfter("Hi");
        mySeq.addAfter("WHERE");
        mySeq.addAfter("ARE");
        mySeq.addAfter("YOU");
        mySeq.addBefore("HEHE");
        mySeq.addBefore("LMO");
        Sequence seqClone = mySeq.clone();
        assertEquals(mySeq.toString(), seqClone.toString());
        assertEquals(mySeq.size(), seqClone.size());
        assertEquals(mySeq.getCurrent(), seqClone.getCurrent());
    }

    @Test //Test ensureCapacity(), tests if inputted was more than original capacity and if it was less that original
    public void testEnsureCapacity(){
        Sequence mySeq = new Sequence(2);
        mySeq.ensureCapacity(5);
        assertEquals(5, mySeq.getCapacity());
        mySeq.ensureCapacity(0);
        assertEquals(5, mySeq.getCapacity());
    }

    @Test//Tests trimToSize(), empty sequence, after adding elements
    public void testTrimToSize(){
        Sequence mySeq = new Sequence(2);
        mySeq.trimToSize();
        assertEquals(mySeq.size(), mySeq.getCapacity());
        mySeq.addAfter("EHm");
        mySeq.addAfter("wha");
        mySeq.addAfter("west");
        assertEquals(mySeq.size(), mySeq.getCapacity());
        Sequence mySeq2 = new Sequence();
        mySeq2.addAfter("EHm");
        mySeq2.addAfter("wha");
        mySeq2.addAfter("west");
        mySeq2.addAfter("noTT");
        assertEquals(10, mySeq2.getCapacity());
        assertEquals(4, mySeq2.size());
        mySeq2.trimToSize();
        assertEquals(mySeq2.size(), mySeq2.getCapacity());
    }


    @Test//Test addAll(), adding two Sequences together, then adding an empty sequence to them, adding two empty sequences
    public void testAddAll(){
        Sequence mySeq = new Sequence();
        mySeq.addAfter("Hi");
        mySeq.addAfter("WHERE");
        mySeq.addAfter("ARE");
        mySeq.addAfter("YOU");
        mySeq.addBefore("HEHE");
        mySeq.addBefore("LMO");
        Sequence mySeq2 = new Sequence();
        mySeq2.addAfter("EHm");
        mySeq2.addAfter("wha");
        mySeq2.addAfter("west");
        mySeq2.addAfter("noTT");
        mySeq2.addAfter("kf");
        mySeq.addAll(mySeq2);
        assertEquals("{Hi, WHERE, ARE, >LMO, HEHE, YOU, EHm, wha, west, noTT, kf} (capacity = 11)", mySeq.toString());
        assertEquals("LMO", mySeq.getCurrent());
        Sequence mySeq1 = new Sequence();
        mySeq.addAll(mySeq1);
        assertEquals("{Hi, WHERE, ARE, >LMO, HEHE, YOU, EHm, wha, west, noTT, kf} (capacity = 11)", mySeq.toString());
        assertEquals("LMO", mySeq.getCurrent());
        Sequence mySeq3 = new Sequence();
        Sequence mySeq4 = new Sequence();
        mySeq3.addAll(mySeq4);
        assertEquals("{} (capacity = 10)", mySeq3.toString());
    }


    @Test//Testing removeCurrent(), current within size, current is the last element, after adding to the beginning
    public void testRemoveCurrent(){
        Sequence mySeq = new Sequence();
        mySeq.addAfter("Hi");
        mySeq.addAfter("WHERE");
        mySeq.addAfter("ARE");
        mySeq.addAfter("YOU");
        mySeq.addBefore("HEHE");
        mySeq.addBefore("LMO");
        assertEquals("LMO", mySeq.getCurrent());
        mySeq.removeCurrent();
        assertEquals("HEHE", mySeq.getCurrent());
        mySeq.removeCurrent();
        mySeq.removeCurrent();
        mySeq.removeCurrent();
        assertEquals(null, mySeq.getCurrent());
        mySeq.addBefore("Zuhair");
        assertEquals("Zuhair", mySeq.getCurrent());
        mySeq.removeCurrent();
        assertEquals("Hi", mySeq.getCurrent());
        mySeq.start();
        mySeq.advance();
        mySeq.advance();
        mySeq.removeCurrent();
        assertEquals(null, mySeq.getCurrent());
    }

    @Test //Testing start() method, empty Sequence, after adding elements, after advancing beyond elements
    public void testStart(){
        Sequence mySeq = new Sequence();
        mySeq.addAfter("Hi");
        mySeq.addAfter("WHERE");
        mySeq.addAfter("ARE");
        mySeq.addAfter("YOU");
        mySeq.addBefore("HEHE");
        mySeq.addBefore("LMO");
        mySeq.start();
        assertEquals("Hi", mySeq.getCurrent());
        Sequence mySeq2 = new Sequence();
        mySeq2.start();
        assertEquals(null, mySeq2.getCurrent());
        Sequence mySeq3 = new Sequence();
        mySeq3.start();
        assertEquals(null, mySeq3.getCurrent());
        mySeq3.addAfter("Hi");
        mySeq3.addAfter("WHERE");
        mySeq3.addAfter("ARE");
        assertEquals("ARE", mySeq3.getCurrent());
        mySeq3.advance();
        mySeq3.advance();
        mySeq3.start();
        assertEquals("Hi", mySeq3.getCurrent());
        mySeq3.advance();
        assertEquals("WHERE", mySeq3.getCurrent());
    }

    @Test//Test clear(), empty sequence, after adding elements
    public void testClear(){
        Sequence mySeq = new Sequence();
        mySeq.clear();
        assertEquals("{} (capacity = 10)", mySeq.toString());
        mySeq.addAfter("Hi");
        mySeq.addAfter("WHERE");
        mySeq.addAfter("ARE");
        mySeq.addAfter("YOU");
        mySeq.addBefore("HEHE");
        mySeq.addBefore("LMO");
        mySeq.clear();
        assertEquals("{} (capacity = 10)", mySeq.toString());
        assertNull(mySeq.getCurrent());
        assertFalse(mySeq.isCurrent());
    }

    @Test//Testing equals, empty sequences, with a cloned sequence, after advancing, after adding before/after
    public void testEquals(){
        Sequence mySeq = new Sequence();
        Sequence mySeq5 = new Sequence();
        assertTrue(mySeq.equals(mySeq5));
        mySeq.addAfter("Hi");
        mySeq.addAfter("WHERE");
        mySeq.addAfter("ARE");
        mySeq.addAfter("YOU");
        mySeq.addBefore("HEHE");
        mySeq.addBefore("LMO");
        Sequence mySeq2 = mySeq.clone();
        assertTrue(mySeq.equals(mySeq2));
        Sequence mySeq1 = mySeq.clone();
        mySeq1.advance();
        assertFalse(mySeq.equals(mySeq1));
        Sequence mySeq3 = mySeq.clone();
        mySeq3.addBefore("wow");
        assertFalse(mySeq.equals(mySeq3));
        Sequence mySeq4 = mySeq.clone();
        mySeq4.removeCurrent();
        mySeq4.addBefore("lelo");
        assertFalse(mySeq.equals(mySeq4));

    }

    @Test //Testing isCurrent. Should return boolean true if Sequence has a currentIndex. false, otherwise.
    public void testIsCurrent(){
        Sequence mySeq = new Sequence();
        assertFalse(mySeq.isCurrent());
        mySeq.addBefore("he");
        assertTrue(mySeq.isCurrent());
        mySeq.advance();
        assertFalse(mySeq.isCurrent());
    }

    @Test //Testing getCapacity before doubling and after doubling
    public void testGetCapacity(){
        Sequence mySeq = new Sequence(3);
        mySeq.addAfter("eh");
        mySeq.addAfter("wo");
        mySeq.addBefore("Why");
        assertEquals(3, mySeq.getCapacity());
        mySeq.addAfter("fw");
        assertEquals(7, mySeq.getCapacity());
    }

    @Test //Testing getCurrent when empty, when isCurrent == true, when adding before/after,when advancing beyond elements
    public void testGetCurrent(){
        Sequence mySeq = new Sequence();
        assertEquals(null, mySeq.getCurrent());
        mySeq.addAfter("wa");
        mySeq.addAfter("ht");
        assertEquals("ht", mySeq.getCurrent());
        mySeq.addBefore("rw");
        assertEquals("rw", mySeq.getCurrent());
        mySeq.advance();
        mySeq.advance();
        assertEquals(null, mySeq.getCurrent());
    }

    @Test //Testing the size() method, empty Sequence, adding element, deleting elements
    public void testSize(){
        Sequence mySeq = new Sequence();
        assertEquals(0, mySeq.size());
        mySeq.addAfter("Hi");
        mySeq.addAfter("WHERE");
        assertEquals(2, mySeq.size());
        mySeq.removeCurrent();
        assertEquals(1, mySeq.size());
        mySeq.addAfter("YOU");
        mySeq.addBefore("HEHE");
        assertEquals(3, mySeq.size());
    }

}
