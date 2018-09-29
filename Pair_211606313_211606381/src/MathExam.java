import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class MathExam {
	//mp��������������ȼ�
	static HashMap<Character, Integer> mp = new HashMap<Character, Integer>();
	static String errorMessage = "����Ĳ�����ʽ�����밴�� -n ���� -grade ����   ���� -grade ����   -n ����  ��ʽ���롣";
	static int grade;
	static StringBuffer[] topic;
	static StringBuffer[] standAnswer;
	
	static char[] Operator = { '+', '-', '��', '��' };
	static {
		mp.put('-',1);
		mp.put('+',1);
		mp.put('��',2);
		mp.put('��',2);
	}

	public static void main(String[] args) throws Exception {
		if(judgmentParameter(args)) {
			int len = Integer.parseInt(args["-n".equals(args[0]) ? 1 : 3]);
			grade = Integer.parseInt(args["-n".equals(args[0]) ? 3 : 1]);
			topic = new StringBuffer[len];
			standAnswer = new StringBuffer[len];
			for(int i = 0;i<len;i++) {
				topic[i] = new StringBuffer();
				standAnswer[i] = new StringBuffer();
			}
			if(grade == 3) {
				for(int i = 0;i < len;i++) {
					Character[] infixExpression = generatingTopic(grade);
					try {
			        	topic[i].append("(" + (i+1) + ")");
			        	standAnswer[i].append("(" + (i+1) + ")");
			        	int res = CalPoland(infixExpression,i);
			        	topic[i].append(System.lineSeparator());
			        	standAnswer[i].append(" = " + res);
			        	if(i != len - 1){
			        		standAnswer[i].append(System.lineSeparator());
			        	}
					} catch (Exception e) {
						// ���ɵ���Ŀ���Ϸ������StringBuffer
						topic[i].setLength(0);
						standAnswer[i].setLength(0);
						i--;
					}
				}
			}else {
				generatingTopicTwo(len,grade);
			}
			try {
				write("out.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Сѧ" + grade + "�꼶��ѧ����Ŀ�����ɣ���鿴out.txt�ļ�");
		}else {
			System.out.println(errorMessage);
		}
	}

	/**
	 * 
	 * @param args �û�����Ĳ���
	 * @return     ������Ҫ��ʱ���� true�����򷵻�false
	 */
	static boolean judgmentParameter(String[] args) {
		if(args.length < 4 || args.length > 4) {
			return false;
		}else {
			// 1  �ж��Ƿ����� -n �� -grade ��ʶ
			if(!(("-n".equals(args[0]) && "-grade".equals(args[2]) )|| ("-grade".equals(args[0]) && "-n".equals(args[2])))) {
				return false;
			}
			
			// 2 ȥ�����ֲ�����ǰ��0
			args[1] = args[1].replaceFirst("^0*", "");
			args[3] = args[3].replaceFirst("^0*", "");
			
			// 3 �������������Ƿ�������
			Pattern pattern = Pattern.compile("[0-9]*");
			boolean matches = pattern.matcher(args["-n".equals(args[0]) ? 1 : 3]).matches();
			
			if (matches && args["-n".equals(args[0]) ? 1 : 3].length() > 4) {
				errorMessage = "��Ŀ�����������������У��������";
				return false;
			} else if (!matches) {
				errorMessage = "��Ŀ�������������������������У�����һ��������";
				return false;
			}
			
			// 4 �����꼶�����Ƿ���1~3
			Pattern compile = Pattern.compile("[1-3]?");
			boolean matches2 = compile.matcher(args["-n".equals(args[0]) ? 3 : 1]).matches();
			
			if (!matches2) {
				errorMessage = "Ŀǰֻ֧��1~3�꼶�����������У�����1~3�е�һ������";
				return false;
			}	    
		}
		return true;
	}
	
	/**
	 * ���ã�����һ���꼶��Ŀ
	 * @param len �û�Ҫ�����ɵ���Ŀ����
	 * @param grade �꼶
	 */
	static void generatingTopicTwo(int len,int grade) {
		for (int i = 0; i < len; i++) {
			// ��ȡ�����������num1,num2��ʾ����������������;
			int num1 = (int) (Math.random() * 100);
			int num2 = (int) (Math.random() * 100);
			
			// symbol�����������;
			int index = (1 == grade) ? ((int) (Math.random() * 10)) % 2 : 2 + ((int) (Math.random() * 2));
			char symbol = Operator[index];
			
			//ȷ���Ͳ�����100
			while(0 == index && num1 + num2 >= 100) {
				num1 = (int) (Math.random() * 100);
				num2 = (int) (Math.random() * 100);
			}
			
			// ������
			int res = 0;
			int remainder = 0; // ����
			switch (symbol) {
			case '+':
				//ȷ��Сѧ1�꼶��ĿΪ��λ���Ӽ���ʮ������λ���Ӽ�һλ��
				if(1 == grade && num1>10 && num2 >10 && num1%10 != 0 && num2%10 !=0) {
					num2 = num2/10*10;
				}
				res = num1 + num2;
				break;
			case '-':		
				// ȷ����һ�����ȵڶ������󣬱���������ָ�����Сѧ�Ӽ��޸���
				if (num1 < num2) {
					int temp = num1;
					num1 = num2;
					num2 = temp;
				}
				//ȷ��Сѧ1�꼶��ĿΪ��λ���Ӽ���ʮ������λ���Ӽ�һλ��
				if(1 == grade && num1>10 && num2 >10 && num2%10 !=0) {
					num2 = num2/10*10;
				}	
				res = num1 - num2;
				break;
			case '��':
				//ȷ��Ϊ���ڳ˷�
				num1 %= 10;
				num2 %= 10;
				res = num1 * num2;
				break;
			case '��':
				//��ֹ����Ϊ0
				while(0 == num2) {
					num2 = (int) (Math.random() * 100);
				}
				
				//ȷ��Ϊ���ڳ���
				if(num2>10) {
					num2 /=10 ;
				}
				
				res = num1 / num2;
				remainder = num1 % num2;
				break;
			}
			// ����Ŀ�ʹ𰸼�¼
			topic[i].append("(" + (i+1) + ") " + num1 + " " + symbol + " " + num2 + System.lineSeparator());
			if (symbol == '/') {
				standAnswer[i].append("(" + (i+1) + ") " + num1 + " " + symbol + " " + num2 + " = " + res
						+ (remainder != 0 ? ("..." + remainder) : ""));
			} else {
				standAnswer[i].append("(" + (i+1) + ") " + num1 + " " + symbol + " " + num2 + " = " + res);
			}
			if(i != len - 1){
        		standAnswer[i].append(System.lineSeparator());
        	}
		}
	}
	
	/**
	 * ���ã��������꼶��Ŀ������
	 * @param len
	 * @param grade
	 * @return 
	 * @throws Exception 
	 */
	static Character[] generatingTopic(int grade){

		// ȷ���������Ͳ��������ܸ���
		int n=5+(int)(Math.random()*3);
		if(n%2 == 0) n++;
		
		Character[] infixExpression = new Character[n];
		
		// ����(n-1)/2������������֤������������ͬ�Ĳ�����
		char c ;
		infixExpression[n-1] = c = Operator[(int)(Math.random()*4)];
		for(int j = 1,k = n-2;j < (n-1)/2;j++,k--) {
			infixExpression[k] = Operator[(int)(Math.random()*4)];
			c ^= infixExpression[k].charValue();
		}
		
		if(c == 0) {
			int q = (int)(Math.random()*4);
			infixExpression[n-1] = Operator[q] != infixExpression[n-1].charValue() ? Operator[q] : Operator[(q+1)%4];
		}
		
		// �����ɵĲ�����������[2,n-2]��Χ���������
		do {
			List<Character> list = new ArrayList<Character>();  
			for(int j = 2;j < infixExpression.length-1;j++){  
	            list.add(infixExpression[j]);  
	        }  
	          
	        Collections.shuffle(list);  
	          
	        Iterator<Character> ite = list.iterator();  
	        int k = 2;
	        while(ite.hasNext()){  
	            infixExpression[k] = ite.next();
	            k++;
	        }
	    }while(isInfixExpre(infixExpression));
		
        return infixExpression;
	}

	/**
	 * ���ã��жϴ˺�׺���ʽ�Ƿ����
	 * @param infix ֻ���������ĺ�׺���ʽ����
	 * @return ���󷵻�true����ȷ����false
	 */
	static boolean isInfixExpre(Character[] infix) {
		if(infix[infix.length-1] == null) {
			return true;
		}
		for(int i = infix.length-1;i>=0;i--) {
			if(infix[i] != null) {
				int temp = 0;
				int count = 0;
				for(int j = i-1;j>=0;j--) {
					if(infix[j] != null) {
						temp++;
					}else {
						count++;
					}
				}
				if(count - temp < 2) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * ���ã�����������Ŀ�������沨�����ʽ
	 * @param infix ��׺���ʽ����
	 * @param n �ڼ�����Ŀ
	 * @param str ����������
	 * @return ���ؼ�����
	 * @throws Exception �����ʽ��������г���ʱ�׳��쳣
	 */
	static int CalPoland(Character infix[],int n) throws Exception{
		ArrayList<Character> chlist = new ArrayList<Character>();
		for (Character ch : infix) {
			if(ch != null) {
				chlist.add(ch);
			}
		}
		Character[] str = new Character[chlist.size()];
//		System.out.println(chlist.toArray());
		for (int i =0 ;i<str.length;i++) {
			str[i] = chlist.get(i);
		}
//		System.out.println();
//		 = (Character[])chlist.toArray();
		
		Stack<Object> s = new Stack<Object>();
		Stack<String> tops = new Stack<String>();
		int k = 1;//�����������
		for(int i = 0; i < infix.length; i++)
		{
			if(infix[i] == null) {
				s.push(' ');
			}else{				
				int a,b;
				String str1,str2;
				if(s.peek() instanceof Character) {
					a = 1 + (int) (Math.random() * 100);
					str1 = " " + a;
				}else {
					a = (int) s.peek();
					str1 = tops.peek();
					tops.pop();
				}
				s.pop();
				
				if(s.peek() instanceof Character) {
					b = 1 + (int) (Math.random() * 100);
					str2 = " " + b;
				}else {
					b = (int) s.peek();
					str2 = tops.peek();
					tops.pop();
				}
				s.pop();
				
				switch(infix[i])
				{
					case '+':
						s.push(b + a);
						break;
					case '-':
						if(b < a ) {
							int temp = b;
							b = a;
							a = temp;
							
							String st = str1;
							str1 = str2;
							str2 = st;
						}
						s.push(b - a);
						break;
					case '��':
						s.push(b * a);
						break;
					case '��':
						if(b % a != 0) {
							throw new Exception();
						}
						s.push(b / a);
						break;
				}
//				System.out.println(s.peek());
				tops.push(str2 + " " + infix[i] + str1);
				int j;
				for(j = k;j<str.length;j++) {
					if(mp.get(infix[i]) < mp.get(str[k].charValue())||(mp.get(infix[i]) == mp.get(str[k].charValue()) && str[k].charValue() == '-')) {
						break;
					}
				}
				if(j<str.length) {
					String s1 = tops.pop();
					tops.push(" (" + s1 + " )");
				}
				k++;
			}
		}
		topic[n].append(tops.peek());
		standAnswer[n].append(tops.pop());
//		System.out.println(s.peek());
		return (int) s.pop();
	}
	
	/**
	 * ���ã������ɵ���Ŀ�ͺʹ�д��ָ���ļ�
	 * @param filename       ��Ҫд����ļ���
	 * @throws IOException   ���ļ������Ϸ�ʱ�׳��쳣
	 */
	static void write(String filename) throws IOException {
		// ����1��ȷ��������ļ���Ŀ�ĵأ�
		// ���filename�а���·��������ȷ��·���Ѵ���
		File file = new File(filename);
		File parentFile = file.getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			parentFile.mkdirs();
			System.out.println("����Ŀ¼��" + parentFile);
		}
		file.createNewFile();
		// ����2������ָ���ļ��������
		OutputStream out = new FileOutputStream(file);
		// ����3��д������
		for (StringBuffer tp : topic) {
			out.write(tp.toString().getBytes());
		}
		out.write(System.lineSeparator().getBytes());
		for (StringBuffer sa : standAnswer) {
			out.write(sa.toString().getBytes());
		}
		// ����4���ر�
		out.close();
	}
}