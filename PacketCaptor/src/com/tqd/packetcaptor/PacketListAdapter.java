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

public class PacketListAdapter extends BaseAdapter {

    Context          mContext;
    LayoutInflater   mLayoutInflater;

    List<Packet>     mPacketList;
    private IPSeeker mIPSeeker;

    public PacketListAdapter(Context context) {
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
        convertView = mLayoutInflater.inflate(R.layout.packet_list_item, parent, false);
        ViewHolder view = new ViewHolder(convertView);

        TextView tvType = (TextView) convertView.findViewById(R.id.item_type);
        // Typeface tf=Typeface.createFromAsset(mContext.getAssets(),
        // "hkww.ttf");
        // tvType.setTypeface(tf);

        if (packet instanceof EthernetPacket) {
            EthernetPacket ePacket = (EthernetPacket) packet;

            tvType.setText("Ethernet");

            // String address=
            // ePacket.getSourceHwAddress()+">>"+ePacket.getDestinationHwAddress();
            view.setText(ePacket.getSourceHwAddress(), ePacket.getDestinationHwAddress());
            // tvAddress.setText(address);
        }
        if (packet instanceof ARPPacket) {

            ARPPacket aPacket = (ARPPacket) packet;

            tvType.setText("ARP");

            String address = aPacket.getSourceHwAddress() + ">>" + aPacket.getDestinationHwAddress();
            view.setText(aPacket.getSourceHwAddress(), aPacket.getDestinationHwAddress());

            // tvAddress.setText(address);
        }
        // 分析ICMP协议
        if (packet instanceof ICMPPacket) {
            ICMPPacket iPacket = (ICMPPacket) packet;

            tvType.setText("ICMP");

            String address = iPacket.getSourceAddress() + ">>" + iPacket.getDestinationAddress();
            String source = iPacket.getSourceAddress();
            String destination = iPacket.getDestinationAddress();
            // String address1=mIPSeeker.getCountry(source)+" "+
            // mIPSeeker.getArea(source)+">>"+mIPSeeker.getCountry(destination)+" "+mIPSeeker.getArea(destination);

            view.setText(source, destination);
            if (mIPSeeker != null)
                view.setAddress(mIPSeeker.getCountry(source), mIPSeeker.getCountry(destination));

        }
        // 分析IP
        if (packet instanceof IPPacket) {
            IPPacket iPacket = (IPPacket) packet;

            String source = iPacket.getSourceAddress();
            String destination = iPacket.getDestinationAddress();
            // String address1=mIPSeeker.getCountry(source)+" "+
            // mIPSeeker.getArea(source)+">>"+mIPSeeker.getCountry(destination)+" "+mIPSeeker.getArea(destination);

            // tvAddress.setText(address1);

            view.setText(source, destination);
            if (mIPSeeker != null)
                view.setAddress(mIPSeeker.getCountry(source), mIPSeeker.getCountry(destination));

            // UDP
            if (iPacket instanceof UDPPacket) {

                UDPPacket udpPacket = (UDPPacket) iPacket;

                tvType.setText("UDP");
                view.setText(source + ":" + udpPacket.getSourcePort(), destination + ":" + udpPacket.getDestinationPort());
                // String address=
                // udpPacket.getSourceAddress()+":"+udpPacket.getSourcePort()+">>"+udpPacket.getDestinationAddress()+":"+udpPacket.getDestinationPort();

                // tvAddress.setText(address);

            }
            if (iPacket instanceof TCPPacket) {
                TCPPacket tcpPacket = (TCPPacket) iPacket;

                tvType.setText("TCP");
                view.setText(source + ":" + tcpPacket.getSourcePort(), destination + ":" + tcpPacket.getDestinationPort());
                // String address=
                // tcpPacket.getSourceAddress()+":"+tcpPacket.getSourcePort()+">>"+tcpPacket.getDestinationAddress()+":"+tcpPacket.getDestinationPort();

                // tvAddress.setText(address);
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

}
