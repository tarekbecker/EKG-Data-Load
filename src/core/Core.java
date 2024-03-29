package core;

import infoobject.InfoObject;

import java.util.ArrayList;

/**
 * Created by Lars on 11.11.2014.
 * The Core represents the global Interface and can manage all actions on the Data Load.
 */
public class Core {
    private static Core singletonInstance = null;
    private final int countWorker = 10;
    private ArrayList<InfoObject> listOfFinishedInfoObjects = new ArrayList<InfoObject>();
    private ArrayList<InfoObject> listOfScheduledInfoObjects = new ArrayList<InfoObject>();
    private ArrayList<Worker> listOfWorker = new ArrayList<Worker>();

    /**
     * Creates an singletonInstance of the core
     * it will initialize a workerpool with 10 threads
     */
    private Core() {
        singletonInstance = this;
        //create the workerpool
        for (int i = 0; i < countWorker; i++) {
            //manage them in a list in case we need to stop them
            listOfWorker.add(new Worker(i));
        }
    }

    /**
     * Will give you access to the Core and its Components
     *
     * @return returns a singletonInstance of the Core
     */
    public static Core getCore() {
        //If no Core object exists, create one
        if (singletonInstance == null) {
            new Core();
        }
        return singletonInstance;
    }

    /**
     * You can add an InfoObject to a list which should be parsed.
     * This can possibly be an WikiArticle or any other subclass of InfoObject.
     * On this Object the object.analyse() method will be called,
     * if a worker is free and takes it.
     *
     * @param object which should be analysed in the future
     */
    public synchronized void addInfoObjectToScheduleList(InfoObject object) {
        //check that it doesn't already exists.
        if (listOfScheduledInfoObjects.contains(object)) {
            return;
        }
        listOfScheduledInfoObjects.add(object);
    }

    /**
     * returns the next Object which can be analysed
     *
     * @return the InfoObject which should be analysed. If the list is empty, returns null
     */
    public synchronized InfoObject getNextInfoObject() {
        if (listOfScheduledInfoObjects.size() < 1) {
            return null;
        }
        return listOfScheduledInfoObjects.remove(0);
    }

    /**
     * Adds an InfoObject to the Core with the mark that it's analysing is finished.
     *
     * @param object which is analysed.
     */
    public synchronized void addInfoObjectToFinished(InfoObject object) {
        listOfFinishedInfoObjects.add(object);
    }
}
