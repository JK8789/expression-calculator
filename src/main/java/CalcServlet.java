
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bsh.EvalError;
import bsh.Interpreter;

@WebServlet("/calc")
public class CalcServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String value = req.getParameter("expression");
		StringBuilder expression = new StringBuilder();
		for (int i = 0; i < value.length(); ++i) {
			char c = value.charAt(i);
			if ((c >= 'a') && (c <= 'z')) {
				char b = req.getParameter(String.valueOf(c)).charAt(0);
				if ((b >= 'a') && (b <= 'z')) { // check if getParameter is name of another variable
					expression.append(req.getParameter(String.valueOf(b)));
				} else {
					expression.append(req.getParameter(String.valueOf(c)));
				}
			} else {
				expression.append(c);
			}
		}

		Interpreter interpreter = new Interpreter();

		String htmlResponse = null;
		Integer integer = null;
		Double d = null;
		try {
			interpreter.eval("result = " + expression);
		} catch (EvalError e2) {
			e2.printStackTrace();
		}
		try {
			integer = (Integer) interpreter.get("result");
		} catch (ClassCastException e) {
			try {
				d = (Double) interpreter.get("result");
			} catch (EvalError e1) {
				e1.printStackTrace();
			}
		} catch (EvalError e) {
			e.printStackTrace();
		}
		PrintWriter writer = resp.getWriter();
		if (integer != null) {
			htmlResponse = Integer.toString(integer);
		} else {
			htmlResponse = Integer.toString(d.intValue());
		}

		writer.write(htmlResponse);

		System.out.println(htmlResponse);
	}
}