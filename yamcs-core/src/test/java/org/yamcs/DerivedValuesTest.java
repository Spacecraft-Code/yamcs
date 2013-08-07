package org.yamcs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.yamcs.management.ManagementService;
import org.yamcs.protobuf.Yamcs.NamedObjectId;
import org.yamcs.tctm.DummyPpProvider;
import org.yamcs.tctm.TcTmService;
import org.yamcs.tctm.TcUplinker;
import org.yamcs.tctm.TmPacketProvider;
import org.yamcs.xtce.Parameter;
import org.yamcs.xtce.XtceDb;
import org.yamcs.xtceproc.XtceDbFactory;

import com.google.common.util.concurrent.AbstractService;

public class DerivedValuesTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        YConfiguration.setup("dvtest");
        ManagementService.setup(false,false);
        XtceDbFactory.reset();
    }

    @Test
    public void testFloatAdd() throws Exception {
        XtceDb db=XtceDbFactory.getInstance("dvtest");
        Parameter fp=db.getParameter("/REFMDB/SUBSYS1/FloatPara11_2");
        assertNotNull(fp);

        RefMdbPacketGenerator tmGenerator=new RefMdbPacketGenerator();
        Channel c=ChannelFactory.create("dvtest", "dvtest", "dvtest", "dvtest", new MyTcTmService(tmGenerator), "dvtest", null);
        ParameterRequestManager prm=c.getParameterRequestManager();
        List<NamedObjectId> paraList=new ArrayList<NamedObjectId>();
        paraList.add(NamedObjectId.newBuilder().setName("test_float_add").build());
        paraList.add(NamedObjectId.newBuilder().setName("/REFMDB/SUBSYS1/FloatPara11_2").build());
        final ArrayList<ParameterValueWithId> params=new ArrayList<ParameterValueWithId>();


        prm.addRequest(paraList, new ParameterConsumer() {
            @Override
            public void updateItems(int subscriptionId,   ArrayList<ParameterValueWithId> items) {
                params.addAll(items);
            }
        });

        c.start();

        long t0 = System.currentTimeMillis();
        int n = 10;
        for(int i = 0 ; i<n; i++)  {
            tmGenerator.generate_PKT11();
        }
        long t1 = System.currentTimeMillis();
        //   s.tryAcquire(5, TimeUnit.SECONDS);
        System.out.println("params0: "+params.get(0));
        System.out.println("params1: "+params.get(1));
        System.out.println("got "+params.size()+" parameters + in "+(t1-t0)/1000.0+" seconds");

        assertEquals(2*n, params.size());
        ParameterValue p=params.get(0).getParameterValue();
        assertEquals(0.1672918, p.getEngValue().getFloatValue(), 0.001);

        p=params.get(1).getParameterValue();
        assertEquals(2.1672918, p.getEngValue().getFloatValue(), 0.001);

        c.quit();
    }

    @Test
    public void testJavascriptFloatAdd() throws Exception {
        XtceDb db=XtceDbFactory.getInstance("dvtest");
        Parameter fp=db.getParameter("/REFMDB/SUBSYS1/FloatPara11_2");
        assertNotNull(fp);

        RefMdbPacketGenerator tmGenerator=new RefMdbPacketGenerator();
        Channel c=ChannelFactory.create("dvtest", "dvtest", "dvtest", "dvtest", new MyTcTmService(tmGenerator), "dvtest", null);
        ParameterRequestManager prm=c.getParameterRequestManager();
        List<NamedObjectId> paraList=new ArrayList<NamedObjectId>();
        paraList.add(NamedObjectId.newBuilder().setName("test_float_add_js").build());
        paraList.add(NamedObjectId.newBuilder().setName("/REFMDB/SUBSYS1/FloatPara11_2").build());

        final ArrayList<ParameterValueWithId> params=new ArrayList<ParameterValueWithId>();


        prm.addRequest(paraList, new ParameterConsumer() {
            @Override
            public void updateItems(int subscriptionId,   ArrayList<ParameterValueWithId> items) {
                params.addAll(items);
            }
        });

        c.start();
        tmGenerator.generate_PKT11();
        //   s.tryAcquire(5, TimeUnit.SECONDS);
        System.out.println("params0: "+params.get(0));
        System.out.println("params1: "+params.get(1));
        assertEquals(2, params.size());
        ParameterValue p=params.get(0).getParameterValue();
        assertEquals(0.1672918, p.getEngValue().getFloatValue(), 0.001);

        p=params.get(1).getParameterValue();
        assertEquals(2.1672918, p.getEngValue().getDoubleValue(), 0.001);

        c.quit();
    }

    @Ignore
    @Test
    //this can be used to see that the performance of javascript is much worse in some later versions of Java 6
    //OpenJDK 7 is very fast.
    public void testJavascriptPerformanceFloatAdd() throws Exception {
        XtceDb db=XtceDbFactory.getInstance("dvtest");
        Parameter fp=db.getParameter("/REFMDB/SUBSYS1/FloatPara11_2");
        assertNotNull(fp);

        RefMdbPacketGenerator tmGenerator=new RefMdbPacketGenerator();
        Channel c=ChannelFactory.create("dvtest", "dvtest", "dvtest", "dvtest", new MyTcTmService(tmGenerator), "dvtest", null);
        ParameterRequestManager prm=c.getParameterRequestManager();
        List<NamedObjectId> paraList=new ArrayList<NamedObjectId>();
        paraList.add(NamedObjectId.newBuilder().setName("test_float_ypr_js").build());
        paraList.add(NamedObjectId.newBuilder().setName("/REFMDB/SUBSYS1/FloatPara11_2").build());

        final ArrayList<ParameterValueWithId> params=new ArrayList<ParameterValueWithId>();


        prm.addRequest(paraList, new ParameterConsumer() {
            @Override
            public void updateItems(int subscriptionId,   ArrayList<ParameterValueWithId> items) {
                params.addAll(items);
            }
        });

        c.start();
        long t0 = System.currentTimeMillis();
        int n = 100000;
        for(int i = 0 ; i<n; i++)  {
            tmGenerator.generate_PKT11();
        }
        long t1 = System.currentTimeMillis();
        //   s.tryAcquire(5, TimeUnit.SECONDS);
        System.out.println("params0: "+params.get(0));
        System.out.println("params1: "+params.get(1));
        System.out.println("got "+params.size()+" parameters + in "+(t1-t0)/1000.0+" seconds");
        assertEquals(2*n, params.size());
        ParameterValue p=params.get(0).getParameterValue();
        assertEquals(0.1672918, p.getEngValue().getFloatValue(), 0.001);

        p=params.get(1).getParameterValue();
        assertEquals(2.1672918, p.getEngValue().getDoubleValue(), 0.001);

        c.quit();
    }
    
    @Test
    public void testSlidingWindow() throws Exception {
        RefMdbPacketGenerator tmGenerator = new RefMdbPacketGenerator();
        Channel c = ChannelFactory.create("dvtest", "dvtest", "dvtest", "dvtest", new MyTcTmService(tmGenerator), "dvtest", null);
        ParameterRequestManager prm = c.getParameterRequestManager();
        
        List<NamedObjectId> subList = Arrays.asList(NamedObjectId.newBuilder().setName("res").build());
        final List<ParameterValueWithId> params = new ArrayList<ParameterValueWithId>();
        prm.addRequest(subList, new ParameterConsumer() {
            @Override
            public void updateItems(int subscriptionId, ArrayList<ParameterValueWithId> items) {
                params.addAll(items);
            }
        });

        c.start();
        tmGenerator.generate_PKT16(1, 2);
        assertEquals(0, params.size()); // Windows:  [*  *  *  1]  &&  [*  2]
        
        tmGenerator.generate_PKT16(2, 4);
        assertEquals(0, params.size()); // Windows:  [*  *  1  2]  &&  [2  4]
        
        tmGenerator.generate_PKT16(3, 6);
        assertEquals(0, params.size()); // Windows:  [*  1  2  3]  &&  [4  6]
        
        // Production starts only when all relevant values for the expression are present
        tmGenerator.generate_PKT16(5, 8);
        assertEquals(1, params.size()); // Windows:  [1  2  3  5]  &&  [6  8] => produce (1 + 5) * 6
        assertEquals(36, params.get(0).getParameterValue().getEngValue().getUint32Value());
        
        params.clear();
        tmGenerator.generate_PKT16(8, 10);
        assertEquals(1, params.size()); // Windows:  [2  3  5  8]  &&  [8 10] => produce (2 + 8) * 8
        assertEquals(80, params.get(0).getParameterValue().getEngValue().getUint32Value());

        c.quit();
    }

    static class MyTcTmService extends AbstractService implements TcTmService {
        TmPacketProvider tm;
        ParameterProvider pp;

        public MyTcTmService(RefMdbPacketGenerator tmGenerator) throws ConfigurationException {
            this.tm=tmGenerator;
            pp=new DummyPpProvider("dvtest", "dvtest");
        }

        @Override
        public TmPacketProvider getTmPacketProvider() {
            return tm;
        }

        @Override
        public TcUplinker getTcUplinker() {
            return null;
        }

        @Override
        public ParameterProvider getParameterProvider() {
            return pp;
        }

        @Override
        protected void doStart() {
            tm.start();
            notifyStarted();
        }

        @Override
        protected void doStop() {
            tm.stop();
            notifyStopped();
        }
    }

    static public class MyDerivedValuesProvider implements DerivedValuesProvider {
        Collection<DerivedValue> dvalues=new ArrayList<DerivedValue>(1);

        public MyDerivedValuesProvider() {
            FloatAddDv dv1=new FloatAddDv("test_float_add", new String[]{"/REFMDB/SUBSYS1/FloatPara11_2", "/REFMDB/SUBSYS1/FloatPara11_3"});
            dvalues.add(dv1);
        }
        @Override
        public Collection<DerivedValue> getDerivedValues() {
            return dvalues;
        }
    }

    static class FloatAddDv extends DerivedValue {
        public FloatAddDv(String name, String[] argnames) {
            super(name, argnames);
        }

        @Override
        public void updateValue() {
            setFloatValue(args[0].getEngValue().getFloatValue()+args[1].getEngValue().getFloatValue());
        }

    }
}
