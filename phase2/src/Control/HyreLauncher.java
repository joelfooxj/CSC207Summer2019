package Control;

import Model.*;
import View.GUI;


import java.io.IOException;
import java.time.LocalDate;


public class HyreLauncher {



    public static void main(String[] args) throws IOException, ClassNotFoundException {
        HyreSession hyreSession = new HyreSession();
        hyreSession.launchSession();
    }

}




