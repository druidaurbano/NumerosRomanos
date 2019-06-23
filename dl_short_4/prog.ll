;LLVM version 3.8.0 (http://llvm.org/)
;program teste
declare i32 @printf(i8*, ...) nounwind
@str_print_int = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
@str_print_double = private unnamed_addr constant [7 x i8] c"%.2lf\0A\00", align 1
define i32 @main() nounwind {
%a = alloca double
store double 0.0, double* %a
%z = alloca double
store double 0.0, double* %z
%v = alloca double
store double 0.0, double* %v
%y = alloca double
store double 0.0, double* %y
%w = alloca i32
store i32 0, i32* %w
%b = alloca double
store double 0.0, double* %b
%1 = sitofp i32 1000 to double
store double %1, double* %a
%2 = sitofp i32 917 to double
store double %2, double* %v
%3 = sitofp i32 3999 to double
store double %3, double* %z
%4 = sitofp i32 140 to double
store double %4, double* %y
store i32 6, i32* %w
store double 20.5, double* %b
%c = alloca double
store double 0.0, double* %c
%5 = load double, double* %b
%6 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds([7 x i8], [7 x i8]* @str_print_double, i32 0, i32 0), double %5)
ret i32 0
}
