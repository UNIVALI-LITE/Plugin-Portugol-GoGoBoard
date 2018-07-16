/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.univali.portugol.plugin.gogoboard;

/**
 *
 * @author Adson Esteves
 */
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class UTMusb 
{
    public static void main(String[] args) 
    {
        Context context = new Context();
        int result = LibUsb.init(context);
        if(result != LibUsb.SUCCESS)
        {
            throw new LibUsbException("Unable to initialize the usb device",result);
        }
        DeviceList list = new DeviceList();
        result = LibUsb.getDeviceList(null, list);
        if(result < 0 )
        {
            throw new LibUsbException("Unable to get device list",result);
        }
        try
        {
            for(Device device : list)
            {
                DeviceDescriptor device_descriptor = new DeviceDescriptor();
                result = LibUsb.getDeviceDescriptor(device, device_descriptor);
                if(result != LibUsb.SUCCESS)
                {
                    throw new LibUsbException("Unable to get device descriptor : ",result);
                }
                DeviceHandle handle = new DeviceHandle();
                result = LibUsb.open(device, handle);
                if (result < 0) {
                    System.out.println(String.format("Unable to open device: %s. "
                        + "Continuing without device handle.",
                        LibUsb.strError(result)));
                    handle = null;
                }
                else
                {
                    System.out.format("Vendor %04x, Product %04x%n", device_descriptor.idVendor(), device_descriptor.idProduct());
                }
            }

        }
        finally
        {
            LibUsb.freeDeviceList(list, true);
        }

    }

}