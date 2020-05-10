import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PropertyTaxComponentsPage, PropertyTaxDeleteDialog, PropertyTaxUpdatePage } from './property-tax.page-object';

const expect = chai.expect;

describe('PropertyTax e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let propertyTaxComponentsPage: PropertyTaxComponentsPage;
  let propertyTaxUpdatePage: PropertyTaxUpdatePage;
  let propertyTaxDeleteDialog: PropertyTaxDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PropertyTaxes', async () => {
    await navBarPage.goToEntity('property-tax');
    propertyTaxComponentsPage = new PropertyTaxComponentsPage();
    await browser.wait(ec.visibilityOf(propertyTaxComponentsPage.title), 5000);
    expect(await propertyTaxComponentsPage.getTitle()).to.eq('realEstateManagementServiceApp.propertyTax.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(propertyTaxComponentsPage.entities), ec.visibilityOf(propertyTaxComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PropertyTax page', async () => {
    await propertyTaxComponentsPage.clickOnCreateButton();
    propertyTaxUpdatePage = new PropertyTaxUpdatePage();
    expect(await propertyTaxUpdatePage.getPageTitle()).to.eq('realEstateManagementServiceApp.propertyTax.home.createOrEditLabel');
    await propertyTaxUpdatePage.cancel();
  });

  it('should create and save PropertyTaxes', async () => {
    const nbButtonsBeforeCreate = await propertyTaxComponentsPage.countDeleteButtons();

    await propertyTaxComponentsPage.clickOnCreateButton();

    await promise.all([
      propertyTaxUpdatePage.setTaxYearInput('5'),
      propertyTaxUpdatePage.setAmountInput('5'),
      propertyTaxUpdatePage.setDatePaidInput('2000-12-31'),
      propertyTaxUpdatePage.setConfirmationNumberInput('confirmationNumber'),
      propertyTaxUpdatePage.buildingSelectLastOption()
    ]);

    expect(await propertyTaxUpdatePage.getTaxYearInput()).to.eq('5', 'Expected taxYear value to be equals to 5');
    expect(await propertyTaxUpdatePage.getAmountInput()).to.eq('5', 'Expected amount value to be equals to 5');
    expect(await propertyTaxUpdatePage.getDatePaidInput()).to.eq('2000-12-31', 'Expected datePaid value to be equals to 2000-12-31');
    expect(await propertyTaxUpdatePage.getConfirmationNumberInput()).to.eq(
      'confirmationNumber',
      'Expected ConfirmationNumber value to be equals to confirmationNumber'
    );

    await propertyTaxUpdatePage.save();
    expect(await propertyTaxUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await propertyTaxComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last PropertyTax', async () => {
    const nbButtonsBeforeDelete = await propertyTaxComponentsPage.countDeleteButtons();
    await propertyTaxComponentsPage.clickOnLastDeleteButton();

    propertyTaxDeleteDialog = new PropertyTaxDeleteDialog();
    expect(await propertyTaxDeleteDialog.getDialogTitle()).to.eq('realEstateManagementServiceApp.propertyTax.delete.question');
    await propertyTaxDeleteDialog.clickOnConfirmButton();

    expect(await propertyTaxComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
