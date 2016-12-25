/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.oleks.pmtalk;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import tech.oleks.pmtalk.bean.FileUpload;
import tech.oleks.pmtalk.bean.Order;
import tech.oleks.pmtalk.service.PmTalkService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

// [START example]
@SuppressWarnings("serial")

@Singleton
public class PmTalkServlet extends HttpServlet {

  @Inject
  PmTalkService pmTalkService;

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    forward("/form.jsp", req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Order o = new Order();
    ServletFileUpload upload = new ServletFileUpload();
    try {
      FileItemIterator it = upload.getItemIterator(req);
      while (it.hasNext()) {
        FileItemStream item = it.next();
        InputStream fieldValue = item.openStream();
        String fieldName = item.getFieldName();
        if ("candidate".equals(fieldName)) {
          String candidate = Streams.asString(fieldValue);
          o.setCandidate(candidate);
        }
        else if ("staffing".equals(fieldName)) {
          String staffingLink = Streams.asString(fieldValue);
          o.setStaffingLink(staffingLink);
        }
        else if ("resume".equals(fieldName)){
          FileUpload resume = new FileUpload();
          ByteArrayOutputStream os = new ByteArrayOutputStream();
          IOUtils.copy(fieldValue, os);
          resume.setStream(new ByteArrayInputStream(os.toByteArray()));
          resume.setContentType(item.getContentType());
          resume.setFileName(item.getName());
          o.setResume(resume);
        }
      }
    } catch (FileUploadException e) {
      e.printStackTrace();
    }

    pmTalkService.minimal(o);
    req.setAttribute("order", o);
    if (o.getErrors() != null) {
      forward("/form.jsp", req, resp);
    }
    else {
      forward("/created.jsp", req, resp);
    }
  }

  private void forward(String url, HttpServletRequest req, HttpServletResponse resp) {
    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
    try {
      dispatcher.forward(req, resp);
    } catch (ServletException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public PmTalkService getPmTalkService() {
    return pmTalkService;
  }

  public void setPmTalkService(PmTalkService pmTalkService) {
    this.pmTalkService = pmTalkService;
  }
}
// [END example]
