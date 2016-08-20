package org.deeplearning4j.rl4j.learning.sync;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author rubenfiszel (ruben.fiszel@epfl.ch) 7/12/16.
 */
public class ExpReplay<A> implements IExpReplay<A> {


    final private Logger log = LoggerFactory.getLogger("Exp Replay");
    final private int batchSize;

    //Implementing this as a circular buffer queue

    private CircularFifoQueue<Transition<A>> storage;

    public ExpReplay(int maxSize, int batchSize) {
        this.batchSize = batchSize;
        storage = new CircularFifoQueue<>(maxSize);
    }

    public ArrayList<Transition<A>> getBatch(int size) {

        Random random = new Random();
        Set<Integer> intSet = new HashSet<>();
        int storageSize = storage.size();
        while (intSet.size() < size) {
            int rd = random.nextInt(storageSize);
            intSet.add(rd);
        }

        ArrayList<Transition<A>> batch = new ArrayList<>(size);
        Iterator<Integer> iter = intSet.iterator();
        while (iter.hasNext()) {
            Transition<A> trans = storage.get(iter.next());
            batch.add(trans.dup());
        }

        return batch;
    }

    public ArrayList<Transition<A>> getBatch() {
        return getBatch(batchSize);
    }

    public void store(Transition<A> transition) {
        storage.add(transition);
        //log.info("size: "+storage.size());
    }



}
