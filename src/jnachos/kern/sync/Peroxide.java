package jnachos.kern.sync;

import jnachos.kern.NachosProcess;
import jnachos.kern.VoidFunctionPtr;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Peroxide
{

    static Semaphore H = new Semaphore("SemH", 0);
    static Semaphore O = new Semaphore("SemO", 0);
    static Semaphore waitO = new Semaphore("waitO", 0);
    static Semaphore mutex = new Semaphore("MUTEX", 1);
    static Semaphore twoOO = new Semaphore("SemOO", 0);
    static Semaphore waitH = new Semaphore("waitH", 0);
    static Semaphore mutex1 = new Semaphore("MUTEX1", 1);
    static long hydrocount = 0;
    static long oxycount = 0;
    static int Hcount, Ocount, nH, nO;


    class HAtom implements VoidFunctionPtr {
        int mID;

         public HAtom(int id)
         {
            mID = id;
         }

        /**
         * oAtom will call oReady. When this atom is used, do continuous
         * "Yielding" - preserving resource
         */
        public void call(Object pDummy) {
            mutex.P();
            if (hydrocount % 2 == 0) // first H atom
            {
                hydrocount++; // increment counter for the first H4
                mutex.V(); // Critical section ended
                H.P(); // Waiting for the second H atom
            } else // second H atom
            {
                hydrocount++; // increment count for next first H
                mutex.V(); // Critical section ended
                H.V(); // wake up the first H atom
                O.V(); // wake up O atom
            }


            waitH.P(); // wait for water message done
            //System.out.println("O atom #" + mID + " used in making water.");
            System.out.println("H atom #" + mID + " making peroxide.");
        }
    }

//same as hydrogen but with the count and mutex is different
    class OAtom implements VoidFunctionPtr {
        int mID;


        public OAtom(int id)
        {
            mID = id;
        }

        /**
         * oAtom will call oReady. When this atom is used, do continuous
         * "Yielding" - preserving resource
         */
        public void call(Object pDummy) {

            mutex.P();
            if (oxycount % 2 == 0) // first H atom
            {
                oxycount++;
                mutex.V();

                twoOO.P();
                waitO.P();
                System.out.println("O atom #" + mID + " making peroxide.");
                //System.out.println("O atom #" + mID + " used in making water.");
                return;
            } else
            {
                oxycount++;
                mutex.V();
                twoOO.V();
                O.P();
            }

            Peroxidecreated();

            waitH.V();
            waitH.V();
            waitO.V();
            mutex1.P();
            Hcount = Hcount - 2;
            Ocount = Ocount - 2;
            //System.out.println("Numbers Left: H Atoms: " + Hcount + ", O Atoms: " + Ocount);
            //System.out.println("Numbers Used: H Atoms: " + (nH - Hcount) + ", O Atoms: " + (nO - Ocount));
            //mutex1.V();

            System.out.println("Numbers Used: Hydrogen Atoms: " + (nH - Hcount) + ", Oxygen Atoms: " + (nO - Ocount));
            System.out.println("Numbers Left: Hydrogen Atoms: " + Hcount + ", Oxygen Atoms: " + Ocount);
            mutex1.V();
            System.out.println("O atom #" + mID + " used in making peroxide.");
        }
    }



    public void createPeroxide()
    {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Number of Hydrogen atoms ? ");
            nH = (new Integer(reader.readLine())).intValue();
            System.out.println("Number of Oxygen atoms ? ");
            nO = (new Integer(reader.readLine())).intValue();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Hcount = nH;
        Ocount = nO;

        int i = 0,j=0;
        while (i < nH) {

            HAtom atom = new HAtom(i);
            (new NachosProcess(new String("hAtom" + i))).fork(atom, null);
            i++;
        }


        while (j < nO) {

            OAtom atom = new OAtom(j);
            (new NachosProcess(new String("oAtom" + j))).fork(atom, null);
            j++;
        }
    }


    public static void Peroxidecreated() {
        System.out.println("Peroxide Molecule made! Splash ");
    }

    public Peroxide() {
        createPeroxide();
    }
    }