
public class Demand2 implements IDemand {

	@Override
	public Character[] operatorGeneration() {
		// ȷ���������Ͳ��������ܸ���
		int n=3;
		Character[] infixExpression = new Character[n];
		// ���������
		int index =  2 + (((int) (Math.random() * 7))%2);
		infixExpression[n-1] = Operator[index];
		return infixExpression;
	}

	@Override
	public void operandGeneration() {
		// TODO �Զ����ɵķ������

	}

}
