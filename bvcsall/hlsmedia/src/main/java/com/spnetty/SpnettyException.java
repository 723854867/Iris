package com.spnetty;

/**
 * Created by djyin on 6/4/2014.
 */
public class SpnettyException extends Throwable {
    public static void main(String[] args) {
        try {
            throw new SpnettyException();
        } catch (RuntimeException r) {
            System.out.println("RuntimeException");
            r.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
        } catch (Error er) {
            System.out.println("Error");
            er.printStackTrace();
        } catch (Throwable t) {
            System.out.println("Throwable");
            t.printStackTrace();
        }
    }
}
