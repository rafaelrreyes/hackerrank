package hackerrank;

import java.util.Arrays;
import java.util.Scanner;

public class hackerrank {

	public static void main (String[] args) {
		
		//doSomething();
		drawSpiralyArray();
		
	}
	
	public static void drawSpiralyArray() {
		int[][] array1 = new int[][] { {2, 4, 6, 8} , 
								{10, 12, 14, 16}, 
								{22, 24, 25, 26}, 
								{30, 31, 32, 33} };
		
		//dir 0 = right, dir 1 = down, dir 2 = left, dir 3 = up
	
		//start with right
		int dir = 0;
		int T, B, L, R;
		
		//top, bottom, right , left indices
		T = 0;
		R =  array1.length - 1;
		B = array1.length -1;
		L = 0;
		
		//top index must never be less than or equal to bot as well as left to right, they basically meet in the middle of the spiral
		while (T <= B &&  L <= R) {
				//left to right, T = row, i = column
			if (dir == 0) {
				for (int i = L; i < R; i++) {
					System.out.println(array1[T][i]);
				}
				//increment down one row
				T++;
				//set to 'down'
				dir = 1;
				
				//top to bot
			} else if (dir == 1) {
				for (int i = T - 1; i < B; i++) {
					System.out.println(array1[i][R]);
				}
				
				//increment left by one
				R--;
				//set to 'left'
				dir = 2;
				
				//right to left
			} else if (dir == 2) {
				for (int i = R - 1; i > L; i--) {
					System.out.println(array1[B][i]);
				}
				
				//increment up by one
				B--;
				//set to 'up'
				dir = 3;
				
				//bottom to top
			} else if (dir == 3) {
				for (int i = B - 1; i > T; i --) {
					System.out.println(array1[i][L]);
				}
				
				//increment right by 1
				L++;
				//set to 'right'
				dir = 0;
			}
		}
		
	}
	
	public static void doSomething() {
		
		Scanner scan = new Scanner(System.in);
		
		int T = scan.nextInt();
		int currentGrowth = 1;
		//number of cycles in each test case
		int[] N = new int[T];
		
		for (int i = 0; i < T; i++) {
			N[i] = scan.nextInt();
			
			//take current number in index i and if number is divisible by 2, take result and loop through and iterate
			for (int k = 0; k <= N[i]; k++) {
				if ( k == 0) {
					;
				} else if ( k % 2 == 0 ) {		//if current cycle is even, add 1
					currentGrowth = currentGrowth + 1;
				} else 								//number is odd, so multiply by 2
					currentGrowth = currentGrowth * 2;
			}
			System.out.println(currentGrowth);
			currentGrowth = 1;
			
		}
	}
	
}



//		double a = 2;
//		int n = 1;
//		int b = 2;
//		
//		Scanner sc = new Scanner(System.in);
//		
//		int testCase =  sc.nextInt();
//		
//		for (int idx = 0; idx < testCase; idx++) {
//			a = sc.nextInt();
//			b = sc.nextInt();
//			n = sc.nextInt();
//			
//			for (int i = 1; i <= n; i++) {
//				
//				a = a + (int)(Math.pow(2, i - 1) * b);
//				System.out.print((int)a + " ");
//			}
//			System.out.println();
//		}
