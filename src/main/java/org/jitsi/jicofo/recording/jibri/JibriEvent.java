/*
 * Jicofo, the Jitsi Conference Focus.
 *
 * Copyright @ 2015 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jitsi.jicofo.recording.jibri;

import org.jitsi.eventadmin.*;

import java.util.*;

/**
 * The events used to notify about the availability and busy/idle status
 * updates of Jibri instances which exist in the current Jicofo session.
 *
 * @author Pawel Domas
 */
public class JibriEvent
    extends Event
{
    /**
     * The event is triggered by {@link JibriDetector} whenever new Jibri is
     * available or when some of the existing Jibri changes the status between
     * idle and busy.
     */
    public static final String STATUS_CHANGED = "org/jitsi/jicofo/JIBRI/STATUS";

    /**
     * The event is triggered by {@link JibriDetector} whenever a Jibri goes
     * down(stops working or disconnects).
     */
    public static final String WENT_OFFLINE = "org/jitsi/jicofo/JIBRI/OFFLINE";

    /**
     * The key for event property which stored the JID of a Jibri(XMPP address).
     */
    private final static String JIBRI_JID_KEY = "jibri.status";

    /**
     * The key for event property which stores a <tt>boolean</tt> holding
     * Jibri's idle status.
     */
    private final static String IS_IDLE_KEY = "jibri.is_idle";

    /**
     * Used to init the properties passed to the constructor.
     * @param jibriJid the Jibri JID(XMPP address)
     * @param isIdle a <tt>Boolean</tt> with Jibri's idle status or
     *        <tt>null</tt> if it should not be included.
     */
    static private Dictionary<String, Object> initDictionary(String    jibriJid,
                                                             Boolean   isIdle)
    {
        Dictionary<String, Object> props = new Hashtable<>();
        props.put(JIBRI_JID_KEY, jibriJid);
        if (isIdle != null)
            props.put(IS_IDLE_KEY, isIdle);
        return props;
    }

    /**
     * Creates {@link #STATUS_CHANGED} <tt>JibriEvent</tt>.
     *
     * @param jibriJid the JID of the Jibri for which the event will be created.
     * @param isIdle a boolean indicating whether the current Jibri's status is
     *        idle (<tt>true</tt>) or busy (<tt>false</tt>).
     *
     * @return {@link #STATUS_CHANGED} <tt>JibriEvent</tt> for given
     *         <tt>jibriJid</tt>.
     */
    static public JibriEvent newStatusChangedEvent(String     jibriJid,
                                                   boolean    isIdle)
    {
        return new JibriEvent(STATUS_CHANGED, jibriJid, isIdle);
    }

    /**
     * Creates {@link #WENT_OFFLINE} <tt>JibriEvent</tt>.
     *
     * @param jibriJid the JID of the Jibri for which the event will be created.
     *
     * @return {@link #WENT_OFFLINE} <tt>JibriEvent</tt> for given
     *         <tt>jibriJid</tt>.
     */
    static public JibriEvent newWentOfflineEvent(String jibriJid)
    {
        return new JibriEvent(WENT_OFFLINE, jibriJid, null);
    }

    /**
     * Checks whether or not given <tt>Event</tt> is a <tt>BridgeEvent</tt>.
     *
     * @param event the <tt>Event</tt> instance to be checked.
     *
     * @return <tt>true</tt> if given <tt>Event</tt> instance is one of bridge
     *         events or <tt>false</tt> otherwise.
     */
    static public boolean isJibriEvent(Event event)
    {
        switch (event.getTopic())
        {
            case STATUS_CHANGED:
            case WENT_OFFLINE:
                return true;
            default:
                return false;
        }
    }

    private JibriEvent(String topic, String jibriJid, Boolean isIdle)
    {
        super(topic, initDictionary(jibriJid, isIdle));
    }

    /**
     * Gets Jibri JID associated with this <tt>JibriEvent</tt> instance.
     *
     * @return <tt>String</tt> which is a JID of the Jibri for which this event
     *         instance has been created.
     */
    public String getJibriJid()
    {
        return (String) getProperty(JIBRI_JID_KEY);
    }

    /**
     * Tells whether or not the current status of the Jibri associated with this
     * event is currently idle (<tt>true</tt>) or busy (<tt>false</tt>).
     *
     * @return a <tt>boolean</tt> value of <tt>true</tt> for idle or
     *         <tt>false</tt> for busy.
     *
     * @throws IllegalStateException if the method is called on
     *         a <tt>JibriEvent</tt> which does not support this property.
     */
    public boolean isIdle()
    {
        Boolean isIdle = (Boolean) getProperty(IS_IDLE_KEY);
        if (isIdle == null)
        {
            throw new IllegalStateException(
                "Trying to access 'isIdle' on wrong event type: " + getTopic());
        }
        return isIdle;
    }
}
