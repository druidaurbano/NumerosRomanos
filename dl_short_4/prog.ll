;LLVM version 3.8.0 (http://llvm.org/)
;program teste
declare i32 @printf(i8*, ...) nounwind
@str_print_int = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
@str_print_double = private unnamed_addr constant [7 x i8] c"%.2lf\0A\00", align 1
define i32 @main() nounwind {
%a = alloca i32
store i32 0, i32* %a
%z = alloca i32
store i32 0, i32* %z
%v = alloca i32
store i32 0, i32* %v
%y = alloca i32
store i32 0, i32* %y
%w = alloca i32
store i32 0, i32* %w
%b = alloca double
store double 0.0, double* %b
%1 = add i32 1, 4
store i32 %1, i32* %a
%2 = add i32 2, 5
store i32 %2, i32* %v
store i32 4999, i32* %z
store i32 140, i32* %y
store i32 6, i32* %w
%3 = fadd double 20.5, 2.0
store double %3, double* %b
%c = alloca double
store double 0.0, double* %c
%4 = load i32, i32* %v
%5 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds([4 x i8], [4 x i8]* @str_print_int, i32 0, i32 0), i32 %4)
ret i32 0
}
