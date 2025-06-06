package app;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CapsLockIndicator
{
	private static final int WAIT_TIME=50;
	private static final String COMMAND="powershell -command [Console]::CapsLock";
    private final Image onImage,offImage;
    private TrayIcon trayIcon;
    public CapsLockIndicator() throws Exception
    {
        onImage=ImageIO.read(getClass().getResource("/on.png"));
        offImage=ImageIO.read(getClass().getResource("/off.png"));
    }
    private Boolean getCapsLockStatus() throws Exception
    {
        Process process=Runtime.getRuntime().exec(COMMAND);
        BufferedReader input=new BufferedReader(new InputStreamReader(process.getInputStream()));
        String output=input.readLine();
        input.close();
        return Boolean.parseBoolean(output);
    }
    private void createTrayIcon(boolean status) throws Exception
    {
        if(status)
            trayIcon=new TrayIcon(onImage);
        else
            trayIcon=new TrayIcon(offImage);
		trayIcon.setToolTip("Caps Lock Indicator");
		trayIcon.setImageAutoSize(true);
        SystemTray.getSystemTray().add(trayIcon);
    }
    private void updateTrayIcon(boolean status)
    {
        if(status)
            trayIcon.setImage(onImage);
        else
            trayIcon.setImage(offImage);
    }
    public void run() throws Exception
    {
        if (!SystemTray.isSupported())
        {
            System.out.println("SystemTray is not supported");
            return;
        }
        boolean status=getCapsLockStatus();
        createTrayIcon(status);
        while(true)
        {
            boolean newStatus=getCapsLockStatus();
            if(newStatus!=status)
            {
                updateTrayIcon(newStatus);
                status=newStatus;
            }
            Thread.sleep(WAIT_TIME);
        }
    }
}