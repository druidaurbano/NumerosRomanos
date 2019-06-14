;LLVM version 3.8.0 (http://llvm.org/)
;program teste
declare i32 @printf(i8*, ...) nounwind
@str_print_int = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
@str_print_double = private unnamed_addr constant [7 x i8] c"%.2lf\0A\00", align 1
define i32 @main() nounwind {
%a = alloca double
store double 0.0, double* %a
%b = alloca double
store double 0.0, double* %b
store double 2.5, double* %a
store double 10.5, double* %b
%c = alloca double
store double 0.0, double* %c
%1 = load double, double* %a
%2 = load double, double* %b
%3 = fadd double %1, %2
store double %3, double* %c
%4 = load double, double* %c
%5 = sitofp i32 10 to double
%6 = fcmp olt double %4, %5
br i1 %6, label %L1, label %L3
L3:
%7 = load double, double* %c
%8 = sitofp i32 20 to double
%9 = fcmp ogt double %7, %8
br i1 %9, label %L1, label %L2
L1:
%10 = load double, double* %c
%11 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds([7 x i8], [7 x i8]* @str_print_double, i32 0, i32 0), double %10)
br label %L2
L2:
ret i32 0
}
