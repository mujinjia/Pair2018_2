
public class Demand3 implements IDemand {

	@Override
	public Character[] operatorGeneration() {
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
		return infixExpression;
	}

	@Override
	public void operandGeneration() {
		

	}

}
