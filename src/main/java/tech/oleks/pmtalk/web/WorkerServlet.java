package tech.oleks.pmtalk.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import tech.oleks.pmtalk.bean.Order;
import tech.oleks.pmtalk.service.PmTalkService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by alexm on 12/25/16.
 */
@Singleton
public class WorkerServlet extends HttpServlet {

    @Inject
    PmTalkService pmTalkService;

    private static final Logger log = Logger.getLogger(WorkerServlet.class.getName());

    protected void doPost(HttpServletRequest r, HttpServletResponse response)
            throws ServletException, IOException {

        Order o = new Order();

        o.setCandidate(
                r.getParameter("candidate"));
        o.setResumeId(
                r.getParameter("resumeId"));
        o.setMeetingLink(
                r.getParameter("meetingLink"));
        o.setStaffingLink(
                r.getParameter("staffingLink"));
        o.setCodingId(
                r.getParameter("codingId"));

        log.info("Worker is processing " + o);

        pmTalkService.complete(o);
    }
}