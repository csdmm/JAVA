package cn.od.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.od.bean.User;
import cn.od.bean.UserFile;
import cn.od.dao.UserDao;
import cn.od.dao.UserFileDao;
import cn.od.util.Const;

public class UserServlet extends HttpServlet {
	
	private static final long serialVersionUID = 2146748539648197507L;
	private UserDao userDao = new UserDao();
	private UserFileDao userFileDao = new UserFileDao();

	public UserServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute(Const.SESSION_USER);
		
		//根君action判断当前用户的操作
		String action = request.getParameter("action");
		//跳转到主页，所有共享
		if (action == null || action.equals("") || action.equals("index")) {
			String filename = request.getParameter("filename");
			List<UserFile> fileList;
			if(filename != null && !filename.equals("")){
				fileList = userFileDao.findSharedFileWithName(filename);
			}else{
				fileList = userFileDao.findSharedFile();
			}
			request.setAttribute("fileList", fileList);
			request.getRequestDispatcher("/WEB-INF/user/index.jsp").forward(request, response);
			
		}else if(action.equals("topUp")){
			//跳转到 充值页面
			request.getRequestDispatcher("/WEB-INF/user/topUp.jsp").forward(request, response);
		}else if(action.equals("doTopUp")){
			//跳转到 充值完成
			user.setMembers(1);
			userDao.doTopUp(user);
			request.getSession().setAttribute(Const.SESSION_USER,user);
			response.sendRedirect("user?action=mydisk");
			return;
		}else if(action.equals("myshare")){
			//跳转到 我的共享
			List<UserFile> fileList = userFileDao.findMySharedFile(user.getId());
			request.setAttribute("fileList", fileList);
			request.getRequestDispatcher("/WEB-INF/user/myshare.jsp").forward(request, response);

		}else if(action.equals("mydisk")){
			//跳转到 我的网盘
			List<UserFile> fileList = userFileDao.findFileListByOwnerId(user.getId(),0);
			request.setAttribute("fileList", fileList);
			request.getRequestDispatcher("/WEB-INF/user/mydisk.jsp").forward(request, response);

		}else if(action.equals("retrieve")){ //重复文件
			List<UserFile> fileList = userFileDao.findRetrieveListByOwnerId(user.getId(),0);
			request.setAttribute("fileList", fileList);
			request.getRequestDispatcher("/WEB-INF/user/mydisk.jsp").forward(request, response);
			return;
		}else if(action.equals("recycle")){
			//跳转到 回收站
			List<UserFile> fileList = userFileDao.findFileListByOwnerId(user.getId(),1);
			request.setAttribute("fileList", fileList);
			request.getRequestDispatcher("/WEB-INF/user/recycle.jsp").forward(request, response);
			return;
		}else if(action.equals("recycleDel")){
			//将字符串数组转为int 数组
			String[] vs = request.getParameterValues("ids");
			int[] ids = strArr2intArr(vs);
			userFileDao.deleteByIds(ids);
			response.sendRedirect("user?action=recycle");
		}else if(action.equals("reductionFile")){
			//还原文件
			String[] vs = request.getParameterValues("ids");
			int[] ids = strArr2intArr(vs);
			userFileDao.updateByIds(ids);
			response.sendRedirect("user?action=mydisk");
			return;

		}else if(action.equals("share")){
			//根据file_ID分享文件
			int id = Integer.parseInt(request.getParameter("id"));
			UserFile userFile = userFileDao.findUserFileById(id);
			//判断文件的所有者,文件主人才能分享
			if(user.getId() == userFile.getOwnerId()){
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String date=simpleDateFormat.format(new Date());
				userFile.setIsShared(2);
				userFile.setSharedTime(date);
				userFileDao.update(userFile);
			}
			response.sendRedirect("user?action=mydisk");
			
		}else if(action.equals("cancelShare")){
			//根据file_ID分享文件
			int id = Integer.parseInt(request.getParameter("id"));
			UserFile userFile = userFileDao.findUserFileById(id);
			//判断文件的所有者,文件主人才能分享
			if(user.getId() == userFile.getOwnerId()){
				userFile.setIsShared(0);
				userFileDao.update(userFile);
			}
			response.sendRedirect("user?action=myshare");
			
		}else if(action.equals("delete")){
			//将字符串数组转为int 数组
			String[] vs = request.getParameterValues("ids");
			int[] ids = strArr2intArr(vs);
			userFileDao.deleteByIdsNo(ids);
			response.sendRedirect("user?action=mydisk");
		}else if(action.equals("edit")){
			request.getRequestDispatcher("/WEB-INF/user/edit.jsp").forward(request, response);
		}
		else if(action.equals("editSubmit")){
			
			String ori_psw = request.getParameter("password");
			String new_psw = request.getParameter("password1");
			
			if(ori_psw.equals(user.getPassword())){
				user.setPassword(new_psw);
				userDao.update(user);
				request.setAttribute("msgSuccess", "密码修改成功！");
				request.getRequestDispatcher("/WEB-INF/user/edit.jsp").forward(request, response);
			}else{
				request.setAttribute("msgFail", "原密码错误！！修改失败！！");
				request.getRequestDispatcher("/WEB-INF/user/edit.jsp").forward(request, response);
			}
		}
			
	}

	
	public int[] strArr2intArr(String[] arr){
		if(arr == null || arr.length == 0)
			return null;
		int[] intArr = new int[arr.length];
		for(int i = 0; i<arr.length;i++){
			intArr[i] = Integer.parseInt(arr[i]);
		}
		return intArr;
	}
}
