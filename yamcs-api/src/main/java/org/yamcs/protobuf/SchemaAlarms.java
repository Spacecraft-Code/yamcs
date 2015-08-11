// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from alarms.proto

package org.yamcs.protobuf;


public final class SchemaAlarms
{

    public static final class AlarmNotice
    {
        public static final org.yamcs.protobuf.SchemaAlarms.AlarmNotice.MessageSchema WRITE =
            new org.yamcs.protobuf.SchemaAlarms.AlarmNotice.MessageSchema();
        public static final org.yamcs.protobuf.SchemaAlarms.AlarmNotice.BuilderSchema MERGE =
            new org.yamcs.protobuf.SchemaAlarms.AlarmNotice.BuilderSchema();
        
        public static class MessageSchema implements io.protostuff.Schema<org.yamcs.protobuf.Alarms.AlarmNotice>
        {
            public void writeTo(io.protostuff.Output output, org.yamcs.protobuf.Alarms.AlarmNotice message) throws java.io.IOException
            {
                if(message.hasType())
                    output.writeEnum(1, message.getType().getNumber(), false);
                if(message.hasTriggerTime())
                    output.writeInt64(2, message.getTriggerTime(), false);
                if(message.hasPval())
                    output.writeObject(3, message.getPval(), org.yamcs.protobuf.SchemaPvalue.ParameterValue.WRITE, false);

                if(message.hasUsername())
                    output.writeString(4, message.getUsername(), false);
            }
            public boolean isInitialized(org.yamcs.protobuf.Alarms.AlarmNotice message)
            {
                return message.isInitialized();
            }
            public java.lang.String getFieldName(int number)
            {
                return org.yamcs.protobuf.SchemaAlarms.AlarmNotice.getFieldName(number);
            }
            public int getFieldNumber(java.lang.String name)
            {
                return org.yamcs.protobuf.SchemaAlarms.AlarmNotice.getFieldNumber(name);
            }
            public java.lang.Class<org.yamcs.protobuf.Alarms.AlarmNotice> typeClass()
            {
                return org.yamcs.protobuf.Alarms.AlarmNotice.class;
            }
            public java.lang.String messageName()
            {
                return org.yamcs.protobuf.Alarms.AlarmNotice.class.getSimpleName();
            }
            public java.lang.String messageFullName()
            {
                return org.yamcs.protobuf.Alarms.AlarmNotice.class.getName();
            }
            //unused
            public void mergeFrom(io.protostuff.Input input, org.yamcs.protobuf.Alarms.AlarmNotice message) throws java.io.IOException {}
            public org.yamcs.protobuf.Alarms.AlarmNotice newMessage() { return null; }
        }
        public static class BuilderSchema implements io.protostuff.Schema<org.yamcs.protobuf.Alarms.AlarmNotice.Builder>
        {
            public void mergeFrom(io.protostuff.Input input, org.yamcs.protobuf.Alarms.AlarmNotice.Builder builder) throws java.io.IOException
            {
                for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
                {
                    switch(number)
                    {
                        case 0:
                            return;
                        case 1:
                            builder.setType(org.yamcs.protobuf.Alarms.AlarmNotice.Type.valueOf(input.readEnum()));
                            break;
                        case 2:
                            builder.setTriggerTime(input.readInt64());
                            break;
                        case 3:
                            builder.setPval(input.mergeObject(org.yamcs.protobuf.Pvalue.ParameterValue.newBuilder(), org.yamcs.protobuf.SchemaPvalue.ParameterValue.MERGE));

                            break;
                        case 4:
                            builder.setUsername(input.readString());
                            break;
                        default:
                            input.handleUnknownField(number, this);
                    }
                }
            }
            public boolean isInitialized(org.yamcs.protobuf.Alarms.AlarmNotice.Builder builder)
            {
                return builder.isInitialized();
            }
            public org.yamcs.protobuf.Alarms.AlarmNotice.Builder newMessage()
            {
                return org.yamcs.protobuf.Alarms.AlarmNotice.newBuilder();
            }
            public java.lang.String getFieldName(int number)
            {
                return org.yamcs.protobuf.SchemaAlarms.AlarmNotice.getFieldName(number);
            }
            public int getFieldNumber(java.lang.String name)
            {
                return org.yamcs.protobuf.SchemaAlarms.AlarmNotice.getFieldNumber(name);
            }
            public java.lang.Class<org.yamcs.protobuf.Alarms.AlarmNotice.Builder> typeClass()
            {
                return org.yamcs.protobuf.Alarms.AlarmNotice.Builder.class;
            }
            public java.lang.String messageName()
            {
                return org.yamcs.protobuf.Alarms.AlarmNotice.class.getSimpleName();
            }
            public java.lang.String messageFullName()
            {
                return org.yamcs.protobuf.Alarms.AlarmNotice.class.getName();
            }
            //unused
            public void writeTo(io.protostuff.Output output, org.yamcs.protobuf.Alarms.AlarmNotice.Builder builder) throws java.io.IOException {}
        }
        public static java.lang.String getFieldName(int number)
        {
            switch(number)
            {
                case 1: return "type";
                case 2: return "triggerTime";
                case 3: return "pval";
                case 4: return "username";
                default: return null;
            }
        }
        public static int getFieldNumber(java.lang.String name)
        {
            java.lang.Integer number = fieldMap.get(name);
            return number == null ? 0 : number.intValue();
        }
        private static final java.util.HashMap<java.lang.String,java.lang.Integer> fieldMap = new java.util.HashMap<java.lang.String,java.lang.Integer>();
        static
        {
            fieldMap.put("type", 1);
            fieldMap.put("triggerTime", 2);
            fieldMap.put("pval", 3);
            fieldMap.put("username", 4);
        }
    }

}
