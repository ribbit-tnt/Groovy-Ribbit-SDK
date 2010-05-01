package com.ribbit.rest

import org.joda.time.DateTime
import com.ribbit.rest.constants.CallStatus

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 2, 2010
 * Time: 11:42:59 PM
 */

class CallLeg {
    // id is the tel: or sip: number
    def id
    CallStatus status
    DateTime startTime
    DateTime answerTime
    DateTime endTime
    String duration
    String mode
    String announce
    boolean playing
    boolean recording

    // List<DtmfEntry> dtmfReceived = new ArrayList<DtmfEntry>();

    List<String> recordings = new ArrayList<String>();

    def transferLeg(callId) {

    }

    def muteLeg() {

    }

    def unmuteLeg() {

    }

    def holdLeg() {

    }

    def unholdLeg() {

    }

    def dropLeg() {

    }

}
