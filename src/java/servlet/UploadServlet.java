/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import agorithms.TopNItemBased;
import agorithms.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author hsb
 */
@WebServlet(name = "Upload", urlPatterns = {"/Upload"})
public class UploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIRECTORY = "upload";
    private static final int THRESHOLD_SIZE = 1024 * 1024 * 3; // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
    private String clientID = "0000";
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // checks if the request actually contains upload file
        Utils util = new Utils();
        if (!ServletFileUpload.isMultipartContent(request)) {
            // if not, we stop here
            return;
        }
        clientID = request.getParameter("clientID");
        // configures some settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(REQUEST_SIZE);

        // constructs the directory path to store upload file
        String uploadPath = getServletContext().getRealPath("")
                + File.separator + UPLOAD_DIRECTORY;
        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String fileName = "";
        String contentType = "";
        String message  = "";
        int maxNumber = 2000;
        int numUserItem[] = new int[2];
        int check = 0;
        double userItem[][] = new double[maxNumber][maxNumber];
        try {
            // parses the request's content to extract file data
            List formItems = upload.parseRequest(request);
            Iterator iter = formItems.iterator();

            // iterates over form's fields
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                // processes only fields that are not form fields
                if (!item.isFormField()) {
                    fileName = new File(item.getName()).getName();
                    contentType = getServletContext().getMimeType(fileName);
                    // fileName = clientID + "";
                    String filePath = uploadPath + File.separator + fileName;
                    File storeFile = new File(filePath);

                    // saves the file on disk
                    item.write(storeFile);
                    if("text/plain".equals(contentType)) {
                        
                        //File file = new File(getServletContext().getRealPath("/upload"));
                        check = util.readFile(userItem, numUserItem, storeFile);
                        if(check == -1) {
                            storeFile.delete();
                            message = "File content is wrong format.";
                        } else {
                            TopNItemBased topNItem = new TopNItemBased();
                            double itemItem[][] = new double[maxNumber][maxNumber];
                            int maxItem = 0;
                            int maxUser = 0;
                            String[] name = fileName.split("\\.");
//                            File f = new File(getServletContext().getRealPath("/WEB-INF/classes/data/" + name[0] + "Model.txt"));
                            check = util.readFile(userItem, numUserItem, storeFile);
                            maxUser = numUserItem[0] + 1;
                            maxItem = numUserItem[1] + 1;
//                            itemItem = topNItem.buildModel(userItem, maxUser, maxItem);
//                            util.writeFile(itemItem, f, maxUser, maxItem);
                            File recommendation = new File(getServletContext().getRealPath("/WEB-INF/classes/data/" + name[0] + "Recommendation.txt"));
                            topNItem.generateTopN(storeFile, recommendation);
                            message = "Upload has been done successfully!";
                            }
                    } else {
                        message = "Wrong file format.";
                    }
                }
            }
            request.setAttribute("message", message);
        } catch (Exception ex) {
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }
        getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
