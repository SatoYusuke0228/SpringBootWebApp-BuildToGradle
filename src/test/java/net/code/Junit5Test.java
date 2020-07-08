package net.code;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JUnit5Test {

	//テスト実行される（エラー）
	@Test
	void fail() {
		Assertions.assertEquals(10, 8);
	}
	//テスト実行される（エラー）
	static class StaticClass {
		@Test
		void fail() {
			Assertions.assertEquals(10, 8);
		}
	}
	//テスト実行される（エラー）
	static class StaticTest {
		@Test
		void fail() {
			Assertions.assertEquals(10, 8);
		}
	}
	//※テスト実行すらされない
	class InnerTest {
		@Test
		void fail() {
			Assertions.assertEquals(10, 8);
		}
	}
	//テスト実行される（成功）
	static class Success {
		@Test
		void success() {
			Assertions.assertEquals(10, 10);
		}

	}
}