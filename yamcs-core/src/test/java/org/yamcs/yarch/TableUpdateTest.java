package org.yamcs.yarch;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.yamcs.utils.parser.ParseException;
import org.yamcs.yarch.streamsql.StreamSqlException;
import org.yamcs.yarch.streamsql.StreamSqlResult;

public class TableUpdateTest extends YarchTestCase {

    @Test(expected = StreamSqlException.class)
    public void testInvalidTable() throws Exception {
        execute("update invalid_table set d =\"new value\"");
    }

    @Test(expected = StreamSqlException.class)
    public void testInvalidValue() throws Exception {
        createTable("invl2");
        execute("update invl2 set d = x");
    }

    @Test(expected = StreamSqlException.class)
    public void testInvalidConversion() throws Exception {
        createTable("invl3");
        execute("update invl3 set d = a");
    }

    @Test(expected = StreamSqlException.class)
    public void testInvalidWhere() throws Exception {
        createTable("invl4");
        execute("update invl4 set d = 'bla' where a");
    }

    @Test(expected = StreamSqlException.class)
    public void testPKupdate() throws Exception {
        createTable("invl4");
        execute("update invl4 set b = 3");
    }

    @Test
    public void testUpdateAll() throws Exception {
        populate("tbl1");
        StreamSqlResult r = ydb.execute("update tbl1 set d ='new value'");
        assertTrue(r.hasNext());
        Tuple t = r.next();
        assertEquals(1000l, t.getLongColumn("inspected"));
        assertEquals(1000l, t.getLongColumn("updated"));

        verify("select * from tbl1",
                (a, b, c, d, e) -> {
                    assertEquals("new value", d);
                }, 1000);
        execute("drop table tbl1");
    }

    @Test
    public void testUpdateIdx() throws Exception {
        populate("tbl12");
        StreamSqlResult r = ydb.execute("update tbl12 set d ='new value' where a=1");
        assertTrue(r.hasNext());
        Tuple t = r.next();
        assertEquals(100l, t.getLongColumn("inspected"));
        assertEquals(100l, t.getLongColumn("updated"));

        verify("select * from tbl12",
                (a, b, c, d, e) -> {
                    if(a==1) {
                        assertEquals("new value", d);
                    } else {
                        assertEquals("r"+a+b+c, d);
                    }
                }, 1000);
        execute("drop table tbl12");
    }

    @Test
    public void testUpdateExpr() throws Exception {
        populate("tbl12");
        StreamSqlResult r = ydb.execute("update tbl12 set d = 'bubu'+b where a>1");
        assertTrue(r.hasNext());
        Tuple t = r.next();
        assertEquals(900l, t.getLongColumn("inspected"));
        assertEquals(800l, t.getLongColumn("updated"));
        
        
        verify("select * from tbl12",
                (a, b, c, d, e) -> {
                    if(a>1) {
                        assertEquals("bubu"+b, d);
                    } else {
                        assertEquals("r"+a+b+c, d);
                    }
                }, 1000);
        execute("drop table tbl12");
    }

    
    @Test
    public void testUpdateIdxLimit() throws Exception {
        populate("tbl12");
        StreamSqlResult r = ydb.execute("update tbl12 set d ='new value' where a=1 limit 2");
        assertTrue(r.hasNext());
        Tuple t = r.next();
        assertEquals(2l, t.getLongColumn("inspected"));
        assertEquals(2l, t.getLongColumn("updated"));

        verify("select * from tbl12",
                (a, b, c, d, e) -> {
                    if(a==1 && b==0 & c<2) {
                        assertEquals("new value", d);
                    } else {
                        assertEquals("r"+a+b+c, d);
                    }
                }, 1000);
        execute("drop table tbl12");
    }

    @Test
    public void testUpdateExtraColumn() throws Exception {
        populate("tbl13");
        StreamSqlResult r = ydb.execute("update tbl13 set e ='new value' where a=1 limit 2");
        assertTrue(r.hasNext());
        Tuple t = r.next();
        assertEquals(2l, t.getLongColumn("inspected"));
        assertEquals(2l, t.getLongColumn("updated"));

        verify("select * from tbl13",
                (a, b, c, d, e) -> {
                    if(a==1 && b==0 & c<2) {
                        assertEquals("new value", e);
                    } else {
                        assertNull(e);
                    }
                }, 1000);
        execute("drop table tbl13");
    }

    private void createTable(String tblName) throws StreamSqlException, ParseException {
        ydb.execute("create table " + tblName
                + "(a int, b int, c int, d string, primary key(a,b,c))");
    }

    private void populate(String tblName) throws Exception {
        int n = 10;
        createTable(tblName);
        ydb.execute("create stream abcd_in(a int, b int, c int, d int)");
        ydb.execute("insert into " + tblName + " select * from abcd_in");
        Stream s = ydb.getStream("abcd_in");

        for (int a = 0; a < n; a++) {
            for (int b = n-1; b >= 0; b--) {
                for (int c = 0; c < n; c++) {
                    s.emitTuple(new Tuple(s.getDefinition(), Arrays.asList(a, b, c, "r" + a + b + c)));
                }
            }
        }
        execute("close stream abcd_in");
    }

    private void verify(String streamQuery, final Checker checker, int numRows) throws Exception {
        ydb.execute("create stream abcd_out as " + streamQuery);

        final AtomicInteger ai = new AtomicInteger(0);
        final Semaphore semaphore = new Semaphore(0);

        Stream s = ydb.getStream("abcd_out");
        s.addSubscriber(new StreamSubscriber() {
            @Override
            public void streamClosed(Stream stream) {
                semaphore.release();
            }

            @Override
            public void onTuple(Stream stream, Tuple tuple) {
                int a = (Integer) tuple.getColumn(0);
                int b = (Integer) tuple.getColumn(1);
                int c = (Integer) tuple.getColumn(2);
                String d = (String) tuple.getColumn(3);
                String e = tuple.size()>4? (String) tuple.getColumn(4): null;

                checker.check(a, b, c, d, e);
                ai.getAndIncrement();
            }
        });
        s.start();
        semaphore.tryAcquire(30, TimeUnit.SECONDS);
        assertEquals(numRows, ai.get());
    }

    interface Checker {
        public void check(int a, int b, int c, String d, String e);
    }
}
