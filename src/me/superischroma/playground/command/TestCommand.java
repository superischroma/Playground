package me.superischroma.playground.command;

import me.superischroma.playground.struct.StructCollection;
import me.superischroma.playground.struct.StructIO;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Command(description = "test lol")
public class TestCommand extends AbstractCommand
{
    @Override
    public void run(String[] args) throws IOException
    {
        StructCollection collection = new StructCollection("Bro");
        collection.setShort("SmallInt", (short) 32767);
        collection.setIntArray("CoolInts", new int[]{83828, 39298, 382});
        collection.setByteArray("CoolBytes", new byte[]{});
        collection.setLongArray("CoolLongs", new long[]{94290973473247L, 42372347832782L});
        Messenger.send(Arrays.toString(collection.asByteArray()));
        StructIO.write(new File("./resources/test.ssf"), collection);
        Messenger.send(StructIO.read(new File("./resources/test.ssf")).toString());
        /*
        StructCollection collection = new StructCollection("TestCollection");
        collection.setString("first", "bruh");
        collection.setDouble("second", 185.494);
        StructCollection nested = new StructCollection("NestedCollection");
        nested.setByte("b", (byte) 5);
        StructCollection dNested = new StructCollection("DoubleNestedCollection");
        dNested.setString("dn", "Wow! A Double Nest!");
        StructCollection tNested = new StructCollection("TripleNestedCollection");
        tNested.setString("tn", "Wow! A TRIPLE Nest!");
        dNested.setCollection(tNested);
        nested.setCollection(dNested);
        collection.setCollection(nested);
        //Messenger.send(Arrays.toString(collection.asByteArray()));
        //Messenger.send(StructIO.read(collection.asByteArray()).toString());
        Messenger.send(new String(collection.asByteArray()));
        //Messenger.send(Arrays.toString(collection.asByteArray()));
        StructIO.write(new File("./resources/test.ssf"), collection);
        Messenger.send(StructIO.read(new File("./resources/test.ssf")).toString());

        //StructIO.write(new File("./resources/test.ssf"), collection);
        //Messenger.send(StructIO.read(new File("./resources/test.ssf")).toString());
        /*
        Messenger.sendnl("[");
        byte[] bytes = collection.asByteArray();
        for (int i = 0; i < bytes.length; i++)
            Messenger.sendnl(i + " - " + bytes[i] + ", ");
        Messenger.send("]");
         */
    }
}