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
%1 = sitofp i32 3 to double
store double %1, double* %a
store double 20.5, double* %b
%c = alloca double
store double 0.0, double* %c
%2 = load double, double* %a
%3 = load double, double* %b
%4 = fadd double %2, %3
store double %4, double* %c
%5 = load double, double* %c
%6 = sitofp i32 10 to double
%7 = fcmp olt double %5, %6
br i1 %7, label %L1, label %L3
L3:
%8 = load double, double* %c
%9 = sitofp i32 20 to double
%10 = fcmp ogt double %8, %9
br i1 %10, label %L1, label %L2
L1:
%11 = load double, double* %c
%12 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds([7 x i8], [7 x i8]* @str_print_double, i32 0, i32 0), double %11)
br label %L2
L2:
ret i32 0
}
