
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

	private static String Calc(HttpServletRequest req) {

		String expr_origin = req.getParameter("expression");
		StringBuilder expression = new StringBuilder();
		for (int i = 0; i < expr_origin.length(); ++i) {
			char c = expr_origin.charAt(i);
			if ((c >= 'a') && (c <= 'z')) {
				char value = req.getParameter(String.valueOf(c)).charAt(0);
				if ((value >= 'a') && (value <= 'z')) { // check if value is name of another variable
					expression.append(req.getParameter(String.valueOf(value)));
				} else {
					expression.append(req.getParameter(String.valueOf(c)));
				}
			} else {
				expression.append(c);
			}
		}

		Interpreter interpreter = new Interpreter();
		Integer integer = null;
		try {
			interpreter.eval("result = " + expression);
		} catch (EvalError e2) {
			return "Bad expression" + expression;
		}
		try {
			integer = (Integer) interpreter.get("result");
		} catch (EvalError e) {
			return "Bad expression" + expression;
		}
		return Integer.toString(integer);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		writer.write(Calc(req));
	}
}