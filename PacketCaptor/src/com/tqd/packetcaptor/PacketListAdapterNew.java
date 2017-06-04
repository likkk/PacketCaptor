package com.tqd.packetcaptor;

import java.util.ArrayList;
import java.util.List;

import com.tqd.utils.IPSeeker;

import net.sourceforge.jpcap.net.ARPPacket;
import net.sourceforge.jpcap.net.EthernetPacket;
import net.sourceforge.jpcap.net.ICMPPacket;
import net.sourceforge.jpcap.net.IPPacket;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.jpcap.net.TCPPacket;
import net.sourceforge.jpcap.net.UDPPacket;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Typeface;

public class PacketListAdapterNew extends BaseAdapter {

    Context          mContext;
    LayoutInflater   mLayoutInflater;

    List<Packet>     mPacketList;
    private IPSeeker mIPSeeker;

    public PacketListAdapterNew(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mPacketList = new ArrayList<Packet>();

    }

    @Override
    public int getCount() {
        return mPacketList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPacketList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Packet packet = mPacketList.get(position);
        convertView = mLayoutInflater.inflate(R.layout.packet_list_item_new, parent, false);
        ViewHolder view = new ViewHolder(convertView);

        TextView tvType = (TextView) convertView.findViewById(R.id.item_type_new);
        TextView ipScr = (TextView)convertView.findViewById(R.id.ipSrc);
        TextView ipDsc= (TextView)convertView.findViewById(R.id.ipDsc);
        TextView data= (TextView)convertView.findViewById(R.id.packet_data);

        if (packet instanceof EthernetPacket) {
            EthernetPacket ePacket = (EthernetPacket) packet;

            tvType.setText("Ethernet");
            ipScr.setText(ePacket.getSourceHwAddress());
            ipDsc.setText(ePacket.getDestinationHwAddress());
        }
        if (packet instanceof ARPPacket) {

            ARPPacket aPacket = (ARPPacket) packet;

            tvType.setText("ARP");
            ipScr.setText(aPacket.getSourceHwAddress());
            ipDsc.setText(aPacket.getDestinationHwAddress());
        }
        // 分析ICMP协议
        if (packet instanceof ICMPPacket) {
            ICMPPacket iPacket = (ICMPPacket) packet;

            tvType.setText("ICMP");
            ipScr.setText(iPacket.getSourceHwAddress());
            ipDsc.setText(iPacket.getDestinationHwAddress());

        }
        // 分析IP
        if (packet instanceof IPPacket) {
            IPPacket iPacket = (IPPacket) packet;
            ipScr.setText(iPacket.getSourceHwAddress());
            ipDsc.setText(iPacket.getDestinationHwAddress());

            // UDP
            if (iPacket instanceof UDPPacket) {

                UDPPacket udpPacket = (UDPPacket) iPacket;

                tvType.setText("UDP");
                data.setText(iPacket.getData().toString());

            }
            if (iPacket instanceof TCPPacket) {
                TCPPacket tcpPacket = (TCPPacket) iPacket;

                tvType.setText("TCP");
                data.setText(tcpPacket.getData().toString());
                data.setText(bytesToHexString(tcpPacket.getTCPData()));
            }

        }
        return convertView;
    }

    public void addPacket(Packet packet) {

        if (packet != null) {
            mPacketList.add(packet);
            this.notifyDataSetChanged();
        }

    }

    class ViewHolder {
        TextView source;
        TextView destination;
        TextView type;
        TextView sourceAddress;
        TextView destinationAddress;

        public ViewHolder(View parent) {

            source = (TextView) parent.findViewById(R.id.source);
            destination = (TextView) parent.findViewById(R.id.destination);
            type = (TextView) parent.findViewById(R.id.item_type);
            sourceAddress = (TextView) parent.findViewById(R.id.sourceaddress);
            destinationAddress = (TextView) parent.findViewById(R.id.destinationaddress);

        }

        public void setText(String s, String d) {
            source.setText("原地址:" + s);
            destination.setText("目的地址:" + d);
        }

        public void setAddress(String s, String d) {
            sourceAddress.setText("地理原地址：" + s);
            destinationAddress.setText("地理目的地址:" + d);
        }

    }

    public void setIPSeeker(IPSeeker mIPSeeker2) {
        this.mIPSeeker = mIPSeeker2;

    }

    public void Reset() {
        mPacketList.clear();
        this.notifyDataSetChanged();
    }
    
    public String bytesToHexString(byte[] src){   
        StringBuilder stringBuilder = new StringBuilder("");   
        if (src == null || src.length <= 0) {   
            return null;   
        }   
        for (int i = 0; i < src.length; i++) {   
            int v = src[i] & 0xFF;   
            String hv = Integer.toHexString(v);   
            if (hv.length() < 2) {   
                stringBuilder.append(0);   
            }   
            stringBuilder.append(hv);   
        }   
        return stringBuilder.toString();   
    }

}
